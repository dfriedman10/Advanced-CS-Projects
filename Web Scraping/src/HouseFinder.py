'''
DSA Spring Final Project: House Finder

This program allows the user to search for liked homes and save liked houses.

Author: Lily Clemens

Date: 5/31/23

Usage:
- Run the program to search for homes in the desired location.
- Like the homes you are interested in.
- View the list of liked homes.
'''

# Import necessary libraries and modules
import time
import tkinter as tk
from PIL import Image, ImageTk
import requests
from io import BytesIO
from bs4 import BeautifulSoup
import re

# Global Variables
# Strings
url = None
lower_budget = None
upper_budget = None
city_text = None
state_text = None
initial_info_frame = None
welcome_label = None
error_label = None
lower_input_label = None
upper_input_label = None
city_input_label = None
city_input = None
state_input_label = None
state_input = None

# Boxes
lower_budget_box = None
upper_budget_box = None

# Buttons
location_enter = None
enter_budget_button = None
refresh_homes = None
message_label = None
like_button = None
back_button = None
restart_button = None

# House Storage
prices = []
homes_info = []
liked_homes = {}

# Increment variable to keep track of house display
i = 0

# State to keep track of the current view (homes or liked)
state = "homes"

# Translate user inputs into standardized dictionary for each state
state_abbreviations = {
    "alabama": "AL",
    "alaska": "AK",
    "arizona": "AZ",
    "arkansas": "AR",
    "california": "CA",
    "colorado": "CO",
    "connecticut": "CT",
    "delaware": "DE",
    "florida": "FL",
    "georgia": "GA",
    "hawaii": "HI",
    "idaho": "ID",
    "illinois": "IL",
    "indiana": "IN",
    "iowa": "IA",
    "kansas": "KS",
    "kentucky": "KY",
    "louisiana": "LA",
    "maine": "ME",
    "maryland": "MD",
    "massachusetts": "MA",
    "michigan": "MI",
    "minnesota": "MN",
    "mississippi": "MS",
    "missouri": "MO",
    "montana": "MT",
    "nebraska": "NE",
    "nevada": "NV",
    "new hampshire": "NH",
    "new jersey": "NJ",
    "new mexico": "NM",
    "new york": "NY",
    "north carolina": "NC",
    "north dakota": "ND",
    "ohio": "OH",
    "oklahoma": "OK",
    "oregon": "OR",
    "pennsylvania": "PA",
    "rhode island": "RI",
    "south carolina": "SC",
    "south dakota": "SD",
    "tennessee": "TN",
    "texas": "TX",
    "utah": "UT",
    "vermont": "VT",
    "virginia": "VA",
    "washington": "WA",
    "west virginia": "WV",
    "wisconsin": "WI",
    "wyoming": "WY"
}

# Take a user-inputted budget
def budget_entered():
    global upper_input_label, lower_input_label, upper_budget_box, lower_budget_box, lower_budget, upper_budget, error_label, enter_budget_button

    # Assign the upper and lower budget limits from the input box
    upper_budget = upper_budget_box.get()
    lower_budget = lower_budget_box.get()

    # Error messages
    invalid_message = "Please enter a valid number."
    range_message = "Please enter a valid range."

    # If the user inputs an invalid number (i.e. non-digit or blank inputs)
    if not upper_budget.isdigit() or not upper_budget or not lower_budget.isdigit() or not lower_budget:
        
        # Display message
        error_label.config(text=invalid_message)
        
        # Reassign variables with corrected value
        upper_budget = upper_budget_box.get()
        lower_budget = lower_budget_box.get()
        return

    # Convert lower_budget and upper_budget to integers
    lower_budget = int(lower_budget)
    upper_budget = int(upper_budget)

    # If the user inputs an invalid range (i.e. the lower budget is larger than the upper budget)
    if lower_budget >= upper_budget:
        
        # Display message
        error_label.config(text=range_message)
        
        # Reassign variables with corrected value
        upper_budget = upper_budget_box.get()
        lower_budget = lower_budget_box.get()
        return

    # If there was previously an error but the user has since inputted a valid number, eliminate the error message
    if str(upper_budget).isdigit() and str(lower_budget).isdigit() and lower_budget < upper_budget and error_label.winfo_ismapped():
        error_label.pack_forget()

    # Hide labels when the valid budget has been inputted
    upper_input_label.pack_forget()
    lower_input_label.pack_forget()
    enter_budget_button.pack_forget()
    upper_budget_box.pack_forget()
    lower_budget_box.pack_forget()

    # Call Determine Location
    determine_location()

# Determine where to search for houses based off user inputted city and state
def determine_location():
    global state_input_label, location_enter, welcome_label

    # Hide the welcome label
    welcome_label.pack_forget()

    # Display new prompting label
    enter_label = tk.Label(initial_info_frame, text="Great! Please enter where you want to live.", font = ("Times New Roman", 16, "bold"))
    enter_label.pack()

    # Display new labels and inputs
    city_input_label.pack()
    city_input.pack()
    state_input_label.pack()
    state_input.pack()

    # Display new button that will call Search Homes
    location_enter.pack()

# Create the URL that will be used to webscrape
def search_homes():
    global url, city_input, state_input, budget, prices, city_text, state_text, initial_info_frame

    # Obtain city and state names from inputs
    city_text = city_input.get()
    state_text = state_input.get()

    # Convert state input to abbreviation if it exists in the dictionary
    state_text = state_abbreviations.get(state_text.lower(), state_text)

    # If the city is two words add a dash to make it compatible with the website's formatting (i.e. San Francisco --> san-francisco) and make it lowercase for simplicity
    if ' ' in city_text:
        city_text = city_text.replace(" ", "-").lower()

    # As long as the user has inputted something for city and state implement those values, along with the upper and lower budget it into the URL
    if len(city_text) != 0 and len(state_text) != 0:
        url = "https://www.compass.com/homes-for-sale/" + city_text + "-" + state_text + "/price=" + str(
            str(lower_budget) + "-" + str(upper_budget) + "/")

        # Hide the initial frame
        initial_info_frame.pack_forget()

        # Call the next method
        scrape_homes()

# Webscrape Compass's website for real estate information
def scrape_homes():
    global homes_info, i, city_text, initial_info_frame, city_input_label, restart_button, root

    # Create a list of all the homes
    homes_info = []

    count = 0

    response = requests.get(url)

    # Check if there is an error with the page
    if response.status_code == 404:
        response_error_label = tk.Label(root, text="I'm sorry, there do not seem to be houses for sale there. Please try somewhere else.", font = ("Times New Roman", 12))
        response_error_label.pack()
        root.update()
        time.sleep(3)
        # If so, restart
        restart()

    # Use Beautiful Soup package
    soup = BeautifulSoup(response.content, 'html.parser')

    # Check if there are no houses available
    no_results_div = soup.find("div", {"data-tn": "noResults"})
    if no_results_div is not None:
        no_results_label = tk.Label(root, text="Sorry, the website does not seem to have any results. Try changing your location or budget.", font = ("Times New Roman", 12))
        no_results_label.pack()
        root.update()
        time.sleep(3)
        
        # If so, restart
        restart()

    # Find all the houses on the website
    homes = soup.find_all('div', {'class': 'uc-listingPhotoCard-body'})

    # Loop through each home in the list and find the relevant information
    for home in homes:

        # Create a dictionary of all the information specific to a home
        home_info = {}
        count += 1

        # Price
        if len(home.find_all("strong")) == 0:
            continue
        price_value = home.find('strong')
        price = price_value.text.strip()
        home_info['price'] = price


        # Images
        image = home.find('img').get('data-fallback-src')
        home_info['images'] = image

        # Address
        address_element = home.find('h2', {'class': 'uc-listingCard-title'})
        
        # Check if an address exists
        if address_element is None:
            address_text = "Unlisted"
            home_info['address'] = address_text
        else:
            address_text = address_element.text.strip()

        # If the city text has two words and has been hyphenated, seperate it again and capitalize it so it will be detectable within the address line
        if '-' in city_text:
            city_text = ' '.join(word.capitalize() for word in city_text.split('-'))
            address_text = address_element.text.strip()

        # If the city text can be found in the address line split it at the city's text (For ex: 200 Jefferson StSan Francisco --> 200 Jefferson St, San Francisco)
        if(city_text in address_text):
                address_text = address_text.split(city_text, 1)
                address_text[1] = city_text + address_text[1]
                home_info['address'] = address_text[0]+", "+address_text[1]

        # If the city text is not in the address line split it at the given neighborhood
        else:
            result = []
            for word in address_text:
                
                # Use regular expression to find two consecutive capital letters in a word
                match = re.search(r'[A-Z]{2}', word)

                if match:
                    
                    # Split the word at the first instance of two capital letters and add the parts to the result
                    parts = re.split(r'([A-Z]{2})', word, maxsplit=1)
                    result.extend(parts)
                else:
                    result.append(word)
            address_text = (result)
            address_text = ''.join(address_text)
            home_info['address'] = address_text

        # Beds
        bed_element = home.find('div', {'class': 'uc-listingCard-subStat uc-listingCard-subStat--beds'})
        
        # Check if the value is null
        if bed_element is None:
            bed_text = "Unlisted"
        
        # Otherwise strip away the letters
        else:
            bed_text = bed_element.text.strip()
            
            # Use regrex to find all numeric characters in the string
            bed_text = re.findall(r'\d+', bed_text)
            
            # Put the extracted number into a single string
            bed_text = ''.join(bed_text)
        home_info['beds'] = bed_text

        # Baths
        bath_element = home.find('div', {'class': 'uc-listingCard-subStat uc-listingCard-subStat--baths'})
        
        # Check if the value is null
        if bath_element is None:
            bath_text = "Unlisted"
        
        # Otherwise strip away the letters
        else:
            bath_text = bath_element.text.strip()
            
            # Use regrex to find all numeric characters in the string
            bath_text = re.findall(r'\d+', bed_text)
            
            # Put the extracted number into a single string
            bath_text = ''.join(bed_text)
        home_info['baths'] = bath_text

        # Add the information of each individual house to the list
        homes_info.append(home_info)
        # Print the scraped data

    # Call the next method
    displayHomes()

# Increment variable i to display houses
def increment():
    global i, state

    # Check the current state and display the corresponding view, incrementing accordingly
    if state == "homes":
        i += 1
        displayHomes()
    elif state == "liked":
        i += 1
        view_liked_homes()

# Download and display images
def display_images(url, home_frame):

    # Account for error in displaying image
    try:
        # Account for url discrepancies
        if url and not url.startswith('http'):
            # Add 'http://' scheme if it's missing
            url = 'http:' + url

        # Find the image in the url
        response = requests.get(url)
        image_data = response.content
        image = Image.open(BytesIO(image_data))

        # Resize the image to a suitable size
        image = image.resize((200, 200))
        photo = ImageTk.PhotoImage(image)

        # Create a frame to contain the image label
        image_frame = tk.Frame(home_frame)
        image_frame.pack()

        # Create a label to display the image
        image_label = tk.Label(image_frame, image=photo)

        # Store a reference to the image to prevent garbage collection
        image_label.image = photo

        # Display the image
        image_label.pack()
    except Exception as e:
        print(f"Error displaying image: {e}")

# Display the homes in homes_info
def displayHomes():
    global refresh_homes, homes_info, home_info, i, state, message_label, initial_info_frame

    # Update the state to "homes" view
    state = "homes"

    # Clear existing labels and destroy the refresh_homes button if it exists
    for widget in root.winfo_children():
        widget.destroy()

    # Display the message label
    message_label = tk.Label(root, text="Here are some options for you!", font=("Times New Roman", 16, "bold"))
    message_label.pack()

    # Check if the root window still exists
    if root.winfo_exists():
        # Clear existing labels and destroy the refresh_homes button if it exists
        for widget in root.winfo_children():
            widget.destroy()

        # Check if the list is empty
        if len(homes_info) == 0:
            message_label = tk.Label(root,
                                     text="Sorry, there doesn't seem to be any houses for sale in that price range. Try changing your budget or location!",
                                     font=("Times New Roman", 16, "bold"))
            message_label.pack()
            restart()
        else:
            # Display the message label
            message_label = tk.Label(root, text="Here are some options for you!", font=("Times New Roman", 16, "bold"))
            message_label.pack()
    else:
        # Root window has been destroyed, stop further execution
        return

    # Create a frame to contain the labels
    frame = tk.Frame(root, width=200, height=200, bg="white")
    frame.pack(pady=10)

    # Create a canvas to display all the house information and to enable scrolling
    canvas = tk.Canvas(frame, width=550, height=500, bg="#89CFF0")
    canvas.pack(side="left", fill="both", expand=False)

    # Create a scrollbar for the canvas
    scrollbar = tk.Scrollbar(frame, orient="vertical", command=canvas.yview, width = 20)
    scrollbar.pack(side="right", fill="y")

    # Configure the canvas to use the scrollbar
    canvas.configure(yscrollcommand=scrollbar.set)

    # Create a new frame inside the canvas
    inner_frame = tk.Frame(canvas, bg="grey")
    inner_frame.place(relx=0.5, rely=0.5, anchor="center")
    canvas.create_window((275, 0), window=inner_frame, anchor="n")

    # Update the scrollbar position to the top
    canvas.yview_moveto(0.0)

    # Check if there are no homes left to display
    if i >= len(homes_info):
        # Destroy the refresh_homes button
        refresh_homes.destroy()

        # Display the message and restart button
        no_homes_label = tk.Label(root, text="No more homes to display. Try again later!",
                                  font=("Times New Roman", 16, "bold"), anchor="n")
        no_homes_label.pack()
        # Allow the user three seconds to view the message before restarting
        time.sleep(3)
        restart()
        
    # Configure the scrollbar to work with the inner frame
    inner_frame.bind("<Configure>", lambda e: canvas.configure(scrollregion=canvas.bbox("all")))

    # Attach the scrollbar to the canvas
    scrollbar.configure(command=canvas.yview)

    # Grid layout for the inner frame
    inner_frame.grid_rowconfigure(0, weight=1)
    inner_frame.grid_columnconfigure(0, weight=1)

    # Display the images within the inner frame using grid
    for j in range(i, min(i + 3, len(homes_info))):
        home_info = homes_info[j]
        home_frame = tk.Frame(inner_frame, borderwidth=2, relief="solid", padx=50, pady=50)
        home_frame.grid(row=j - i, column=0, padx=10, pady=10)

        # Configure the size of the home frame
        home_frame.columnconfigure(0, weight=1)
        home_frame.rowconfigure(40, weight=1)

        # Create the index label
        index_label = tk.Label(home_frame, text="Home " + str(j + 1), font = ("Times New Roman", 16, "bold"))
        index_label.pack(anchor="n")

        home_label = tk.Label(home_frame,
                              text=f"Price: {home_info['price']}\nAddress: {home_info['address']}\nBeds: {home_info['beds']}\nBaths: {home_info['baths']}", font = ("Times New Roman", 12, "bold"))
        home_label.pack()

        # Create the Like button for each house
        like_button = tk.Button(home_frame, text="Like", command=lambda index=j: like_home(index), font = ("Times New Roman", 12))
        like_button.pack(pady=5)

        # Display the images within the home_frame
        display_images(home_info['images'], home_frame)

    # Increment the index for the next set of homes
    i += 3

    # Configure the canvas to update scrollable region
    inner_frame.update_idletasks()
    canvas.configure(scrollregion=canvas.bbox("all"))

    # Create the refresh_homes button
    refresh_homes = tk.Button(root, text="Refresh", command=increment, font = ("Times New Roman", 12))
    refresh_homes.pack(side="bottom")

    # Create the View Liked Homes button
    view_liked_button = tk.Button(root, text="View Liked Homes", command=view_liked_homes, font = ("Times New Roman", 12))
    view_liked_button.pack(side="bottom")

# Add a home to liked_homes to view later
def like_home(index):
    liked_homes[index] = homes_info[index]

# Return from viewing liked homes to the main display screen
def go_back_liked():
    global i, state

    # Reset the index to display homes from the previous position
    i -= 3

    # Update the state to "homes" view
    state = "homes"

    # Display the homes view
    displayHomes()

# View all the homes that you have liked
def view_liked_homes():
    global liked_homes, state, refresh_homes, message_label

    # If there are no homes to view
    if len(liked_homes)==0:
        message_label.config(text="You currently don't have any liked houses to view. Try liking a few!", font=("Times New Roman", 16, "bold"), anchor="n")
        root.update()

        # Allow the user three seconds to view the message before refreshing
        time.sleep(2)
        go_back_liked()

    # If there are homes to view
    else:
        # Destroy the refresh button
        refresh_homes.destroy()

        # Update the state to "liked" view
        state = "liked"

        # Clear existing labels
        for widget in root.winfo_children():
            widget.destroy()

        # Display the message label
        liked_homes_label = tk.Label(root, text="Here are your liked homes:", font = ("Times New Roman", 16, "bold"))
        liked_homes_label.pack()

        # Create a frame to contain the labels
        frame = tk.Frame(root, width=200, height=200, bg="white")
        frame.pack(pady=10)

        # Create a canvas to enable scrolling
        canvas = tk.Canvas(frame, width=550, height=500, bg="pink")
        canvas.pack(side="left", fill="both", expand=False)

        # Create a scrollbar for the canvas
        scrollbar = tk.Scrollbar(frame, orient="vertical", command=canvas.yview, width=20)
        scrollbar.pack(side="right", fill="y")

        # Configure the canvas to use the scrollbar
        canvas.configure(yscrollcommand=scrollbar.set)

        # Create a new frame inside the canvas
        inner_frame = tk.Frame(canvas, bg="grey")
        inner_frame.place(relx=0.5, rely=0.5, anchor="center")
        canvas.create_window((275, 0), window=inner_frame, anchor="center")

        # Configure the scrollbar to work with the inner frame
        inner_frame.bind("<Configure>", lambda e: canvas.configure(scrollregion=canvas.bbox("all")))

        # Attach the scrollbar to the canvas
        scrollbar.configure(command=canvas.yview)

        # Grid layout for the inner frame
        inner_frame.grid_rowconfigure(0, weight=1)
        inner_frame.grid_columnconfigure(0, weight=1)

        # Display each liked home in the same box format
        for index, home in liked_homes.items():
            home_frame = tk.Frame(inner_frame, borderwidth=2, relief="solid", padx=50, pady=50)
            home_frame.grid(row=index, column=0, padx=10, pady=10)

            # Configure the size of the home frame
            home_frame.columnconfigure(0, weight=1)
            home_frame.rowconfigure(40, weight=1)

            # Create the index label
            index_label = tk.Label(home_frame, text="Home " + str(index + 1), font=("Times New Roman", 16, "bold"))
            index_label.pack(anchor="n")

            home_label = tk.Label(home_frame,
                                  text=f"Price: {home['price']}\nAddress: {home['address']}\nBeds: {home['beds']}\nBaths: {home['baths']}",
                                  font=("Times New Roman", 12, "bold"))
            home_label.pack()

            # Display the images within the home_frame
            display_images(home['images'], home_frame)

        # Configure the canvas to update scrollable region
        inner_frame.update_idletasks()
        canvas.configure(scrollregion=canvas.bbox("all"))

        # Create the back button
        back_button = tk.Button(root, text="Back", command=go_back_liked, font = ("Times New Roman", 12))
        back_button.pack(side="bottom")

# Restart the program
def restart():
    global root
    root.destroy()
    main()

# Main method to allow the program to restart
def main():
    # Implement all global variables
    global root, welcome_label, initial_info_frame, lower_input_label, upper_input_label, upper_budget_box, lower_budget_box, error_label
    global enter_budget_button, city_input_label, city_input, state_input_label, state_input, location_enter, refresh_homes, restart_button, message_label

    # Create the graphics root
    root = tk.Tk()
    root.resizable(False, False)
    root.title("House Finder")
    root.geometry("600x600")

    # Create a frame
    initial_info_frame = tk.Frame(root)
    initial_info_frame.pack(pady=10)

    # Create and display a welcome label in the frame
    welcome_label = tk.Label(initial_info_frame, text="Welcome to House Finder!", font=("Times New Roman", 16, "bold"))
    welcome_label.place(x=200, y=20)
    welcome_label.pack()

    # Initialize budget variables
    error_label = tk.Label(root, text="", font = ("Times New Roman", 12))
    lower_input_label = tk.Label(initial_info_frame, text="Enter your minimum price:", font = ("Times New Roman", 12))
    lower_budget_box = tk.Entry(initial_info_frame)
    upper_input_label = tk.Label(initial_info_frame, text="Enter your upper price:", font = ("Times New Roman", 12))
    upper_budget_box = tk.Entry(initial_info_frame)
    enter_budget_button = tk.Button(initial_info_frame, text="Enter", command=budget_entered, font =("Times New Roman", 12))

    # Pack budget variables
    error_label.pack()
    lower_input_label.pack()
    lower_budget_box.pack()
    upper_input_label.pack()
    upper_budget_box.pack()
    enter_budget_button.pack()

    # Initialize determine location variables
    city_input_label = tk.Label(initial_info_frame, text="Enter your city:", font = ("Times New Roman", 12))
    city_input = tk.Entry(initial_info_frame)
    state_input_label = tk.Label(initial_info_frame, text="Enter your state:", font = ("Times New Roman", 12))
    state_input = tk.Entry(initial_info_frame)
    location_enter = tk.Button(initial_info_frame, text="Enter", command=search_homes, font = ("Times New Roman", 12))

    # Initialize display home variables
    refresh_homes = tk.Button(root, text="Refresh New Options", command=increment, font = ("Times New Roman", 12))
    restart_button = tk.Button(root, text="Restart", command=restart, font = ("Times New Roman", 12))

    # Begin the main loop of the GUI
    root.mainloop()

# Call the entry function to start the program
if __name__ == "__main__":
    main()


