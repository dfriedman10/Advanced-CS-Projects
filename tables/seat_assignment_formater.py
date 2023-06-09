ADULT_TITLES = ["Mr.", "Ms.", "Dr."]
NUM_LINES_PER_PAGE = 50
INTRO_NUM_OF_LINES = 2

def isAdult(name):
    try:
        #grab their title, if they have one
        title = name[0:name.index(' ')]
    except:
        #if it's not an adult, they might not have a space in their name
        return False
    return title in ADULT_TITLES

def writeTableIntro(f, introString, currLineNumber):
    f.write(introString + "\n")
    i = 0
    separatorString = ""
    while i < len(introString):
        separatorString += "-"
        i += 1
    f.write(separatorString + "\n")
    currLineNumber += INTRO_NUM_OF_LINES
    return currLineNumber

def makeSpaceForNextTable(f, currLineNumber, numOfLinesInTable):
    if currLineNumber + numOfLinesInTable > NUM_LINES_PER_PAGE:
        while currLineNumber < NUM_LINES_PER_PAGE:
            f.write("\n")
            currLineNumber += 1
        currLineNumber = 0
    return currLineNumber

def writeTableAssignmentsDoc(tableArray):
    f = open("table_assignments.txt", 'w')
    currTableNumber = 1
    currLineNumber = 0
    for table in tableArray:
        firstStudent = True
        numOfAdults = 0
        introString = "Table " + str(currTableNumber) + ":"
        for person in table:
            if isAdult(person):
                if numOfAdults == 0:
                    introString += " " + person
                else:
                    introString += " and " + person
                numOfAdults += 1

            else:   
                if firstStudent:
                    # make sure there's enough room, then write the table intro
                    numOfLinesInTable = len(table) - numOfAdults + INTRO_NUM_OF_LINES
                    currLineNumber = makeSpaceForNextTable(f, currLineNumber, numOfLinesInTable)
                    currLineNumber = writeTableIntro(f, introString, currLineNumber)
                    firstStudent = False

                f.write(person + "\n")
                currLineNumber += 1

        currTableNumber += 1

        # if we're getting close to the end of the page, add the appropriate num of line breaks
        if currLineNumber == NUM_LINES_PER_PAGE:
            currLineNumber = 0
        elif currLineNumber == NUM_LINES_PER_PAGE - 1:
            f.write("\n")
            currLineNumber = 0
        else:
            f.write("\n\n")
            currLineNumber += 2

    f.close()
    