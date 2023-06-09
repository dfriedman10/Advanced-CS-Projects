import csv
from random import randint
from seat_assignment_formater import *

insideNums = list(range(0, 32))

def numStudents():
    sum = 0
    for grade in students:
        sum += len(grade)
    return sum

def tableCount():
    sum = 0
    for t in tables:
        sum += len(t)
    print(sum)

def putUsAt25():
    insideNums.remove(24)
    tables[24] = familyFaculty.pop(3)

reader = open("tables/NewRoster.txt", "r")

singleFaculty = []
familyFaculty = []
facultyNeedPair = []
facultyOutside = []
facultyDone = False

students = []

for row in reader:
    row = row.strip()

    if row == '---':
        if not facultyDone:
            facultyDone = True
        else:
            students.append(currClass)
        currClass = []
        continue

    if not facultyDone:

        currTable = []
        for fac in row.split(','):
            currTable.append(fac.strip())

        if currTable[0].strip() == 'PAIR':
            facultyNeedPair.append(currTable[1:])
        elif currTable[0].strip() == 'OUTSIDE':
            facultyOutside.append(currTable[1:])
        elif len(currTable) == 1:
            singleFaculty.append(currTable)
        else:
            familyFaculty.append(currTable)

    else:
        name = row.split(',')
        currClass.append(name[1] + " " + name[0])

tables = []
for i in range(0, 38):
    tables.append([])

# putUsAt25()


while len(facultyNeedPair) > 0:
    randFac1 = facultyNeedPair.pop(randint(0, len(facultyNeedPair)-1))
    randFac2 = singleFaculty.pop(randint(0, len(singleFaculty)-1))
    randNum = insideNums.pop(randint(0, len(insideNums)-1))

    for f in randFac1:
        tables[randNum].append(f)
    for f in randFac2:
        tables[randNum].append(f)


while len(familyFaculty) > 0:
    randFac = familyFaculty.pop(randint(0, len(familyFaculty)-1))
    randNum = insideNums.pop(randint(0, len(insideNums)-1))

    for f in randFac:
        tables[randNum].append(f)

while len(insideNums) > 0:
    randFac = singleFaculty.pop(randint(0, len(singleFaculty)-1))
    randNum = insideNums.pop(randint(0, len(insideNums)-1))

    for f in randFac:
        tables[randNum].append(f)

insideNums = list(range(0, 32))
while len(singleFaculty) > 0:
    randNum = insideNums[randint(0, len(insideNums)-1)]

    if len(tables[randNum]) > 1:
        continue

    insideNums.remove(randNum)
    randFac = singleFaculty.pop(randint(0, len(singleFaculty)-1))

    for f in randFac:
        tables[randNum].append(f)


outsideNums = list(range(32, 38))
while len(outsideNums) > 0:
    randFac = facultyOutside.pop(randint(0, len(facultyOutside)-1))
    randNum = outsideNums.pop(randint(0, len(outsideNums)-1))

    if len(tables[randNum]) > 1:
        continue

    for f in randFac:
        tables[randNum].append(f)


startTable = randint(0, len(tables)-1)

while numStudents() > 0:

    grades = [0, 1, 2, 3]

    while len(grades) > 0:
        grade = grades.pop(randint(0, len(grades)-1))

        for i in range(len(tables)):
            if len(students[grade]) == 0:
                break
            t = (startTable + i) % len(tables)
            if len(tables[t]) < 8:
                tables[t].append(students[grade].pop(randint(0, len(students[grade])-1)))

tableCount()
writeTableAssignmentsDoc(tables)