import tkinter as tk
from tkinter import ttk, messagebox

import requests

# ============================
# ======= HTTP CLIENT ========
# ============================

# Base URL for the API
BASE_URL = 'http://localhost:8080/api'

# Global variable to store the session cookie
session_cookie = None
session_cookie_name = 'JSESSIONID'


# GET request
def get_request(endpoint, headers={}):
    url = BASE_URL + endpoint
    if session_cookie:
        headers['Cookie'] = f'{session_cookie_name}={session_cookie}'
    response = requests.get(url, headers=headers)
    return response


# POST request
def post_request(endpoint, data, headers={}):
    url = BASE_URL + endpoint
    if session_cookie:
        headers['Cookie'] = f'{session_cookie_name}={session_cookie}'
    response = requests.post(url, json=data, headers=headers)
    return response


# POST request with form data
def post_request_with_form(endpoint, form_data, headers={}):
    url = BASE_URL + endpoint
    if session_cookie:
        headers['Cookie'] = f'{session_cookie_name}={session_cookie}'
    response = requests.post(url, data=form_data, headers=headers)
    return response


# PUT request
def put_request(endpoint, data, headers={}):
    url = BASE_URL + endpoint
    if session_cookie:
        headers['Cookie'] = f'{session_cookie_name}={session_cookie}'
    response = requests.put(url, json=data, headers=headers)
    return response


# DELETE request
def delete_request(endpoint):
    url = BASE_URL + endpoint
    headers = {}
    if session_cookie:
        headers['Cookie'] = f'{session_cookie_name}={session_cookie}'
    response = requests.delete(url, headers=headers)
    return response


# ============================
# ======= APPLICATION ========
# ============================

class Application(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Bank application")
        self.geometry("800x600")
        self.columnconfigure(0, weight=1)
        self.rowconfigure(0, weight=1)

        self.main_frame = ttk.Frame(self)

        self.create_home_page()

    def create_home_page(self):
        self.clear_main_frame()
        self.main_frame.grid(row=0, column=0, padx=50, pady=50, sticky="")
        self.main_frame.columnconfigure(0, weight=1)
        self.main_frame.rowconfigure((0, 1, 2), weight=1)

        self.label = ttk.Label(self.main_frame, text="Bank application")
        self.label.grid(row=0, column=0, pady=10)

        self.login_button = ttk.Button(self.main_frame, text="Login", command=self.show_login_page)
        self.login_button.grid(row=1, column=0, pady=5)

        self.register_button = ttk.Button(self.main_frame, text="Register", command=self.show_register_page)
        self.register_button.grid(row=2, column=0, pady=5)

    def show_login_page(self):
        self.clear_main_frame()
        self.main_frame.grid(row=0, column=0, padx=50, pady=50, sticky="")
        self.main_frame.columnconfigure(0, weight=1)
        self.main_frame.rowconfigure((0, 1, 2), weight=1)

        self.label = ttk.Label(self.main_frame, text="Login Page")
        self.label.grid(row=0, column=1, pady=10)

        self.login_label = ttk.Label(self.main_frame, text="Login:")
        self.login_label.grid(row=1, column=0, padx=10, pady=5)

        self.login_entry = ttk.Entry(self.main_frame)
        self.login_entry.grid(row=1, column=1, padx=10, pady=5)

        self.password_label = ttk.Label(self.main_frame, text="Password:")
        self.password_label.grid(row=2, column=0, padx=10, pady=5)

        self.password_entry = ttk.Entry(self.main_frame, show="*")
        self.password_entry.grid(row=2, column=1, padx=10, pady=5)

        self.button_frame = ttk.Frame(self.main_frame)
        self.button_frame.grid(row=3, column=1, padx=5, pady=5)
        self.button_frame.columnconfigure((0, 1), weight=1)

        self.login_button = ttk.Button(self.button_frame, text="Login", command=self.login)
        self.login_button.grid(row=0, column=0, pady=5)

        self.back_button = ttk.Button(self.button_frame, text="Back", command=self.create_home_page)
        self.back_button.grid(row=0, column=1, pady=5)

    def login(self):
        global session_cookie

        form_data = {
            'login': self.login_entry.get(),
            'password': self.password_entry.get()
        }
        response = post_request_with_form('/login', form_data)

        if response.status_code == requests.codes.ok:
            print("Login successful")
            session_cookie = response.cookies.get(session_cookie_name)
            self.show_main_page()
        else:
            print(f"Login failed with status code {response.status_code}")
            tk.messagebox.showerror("Login Failed", "Invalid credentials")

    def show_register_page(self):
        self.clear_main_frame()
        self.main_frame.grid(row=0, column=0, padx=50, pady=50, sticky="")
        self.main_frame.columnconfigure(0, weight=1)
        self.main_frame.rowconfigure((0, 1, 2), weight=1)

        self.label = ttk.Label(self.main_frame, text="Register Page")
        self.label.grid(row=0, column=1, pady=10)

        self.login_label = ttk.Label(self.main_frame, text="Login:")
        self.login_label.grid(row=1, column=0, padx=10, pady=5)

        self.login_entry = ttk.Entry(self.main_frame)
        self.login_entry.grid(row=1, column=1, padx=10, pady=5)

        self.password_label = ttk.Label(self.main_frame, text="Password:")
        self.password_label.grid(row=2, column=0, padx=10, pady=5)

        self.password_entry = ttk.Entry(self.main_frame, show="*")
        self.password_entry.grid(row=2, column=1, padx=10, pady=5)

        self.branch_label = ttk.Label(self.main_frame, text="Branch name:")
        self.branch_label.grid(row=3, column=0, padx=10, pady=5)

        response = get_request("/branch")
        branches = response.json()
        self.branch_combobox = ttk.Combobox(self.main_frame, values=[branch['name'] for branch in branches],
                                            state='readonly')
        self.branch_combobox.grid(row=3, column=1, padx=10, pady=5)

        self.first_name_label = ttk.Label(self.main_frame, text="First name:")
        self.first_name_label.grid(row=4, column=0, padx=10, pady=5)

        self.first_name_entry = ttk.Entry(self.main_frame)
        self.first_name_entry.grid(row=4, column=1, padx=10, pady=5)

        self.second_name_label = ttk.Label(self.main_frame, text="Second name:")
        self.second_name_label.grid(row=5, column=0, padx=10, pady=5)

        self.second_name_entry = ttk.Entry(self.main_frame)
        self.second_name_entry.grid(row=5, column=1, padx=10, pady=5)

        self.last_name_label = ttk.Label(self.main_frame, text="Last name:")
        self.last_name_label.grid(row=6, column=0, padx=10, pady=5)

        self.last_name_entry = ttk.Entry(self.main_frame)
        self.last_name_entry.grid(row=6, column=1, padx=10, pady=5)

        self.email_label = ttk.Label(self.main_frame, text="Email:")
        self.email_label.grid(row=7, column=0, padx=10, pady=5)

        self.email_entry = ttk.Entry(self.main_frame)
        self.email_entry.grid(row=7, column=1, padx=10, pady=5)

        self.date_of_birth_label = ttk.Label(self.main_frame, text="Date of birth:")
        self.date_of_birth_label.grid(row=8, column=0, padx=10, pady=5)

        self.date_of_birth_entry = ttk.Entry(self.main_frame)
        self.date_of_birth_entry.grid(row=8, column=1, padx=10, pady=5)

        self.place_of_birth_label = ttk.Label(self.main_frame, text="Place of birth:")
        self.place_of_birth_label.grid(row=9, column=0, padx=10, pady=5)

        self.place_of_birth_entry = ttk.Entry(self.main_frame)
        self.place_of_birth_entry.grid(row=9, column=1, padx=10, pady=5)

        self.gender_label = ttk.Label(self.main_frame, text="Gender:")
        self.gender_label.grid(row=10, column=0, padx=10, pady=5)

        self.gender_frame = ttk.Frame(self.main_frame)
        self.gender_frame.grid(row=10, column=1, padx=5, pady=5)
        self.gender_frame.columnconfigure((0, 1), weight=1)

        self.gender_var = tk.StringVar()
        self.male_radio = ttk.Radiobutton(self.gender_frame, text="Male", variable=self.gender_var, value="male")
        self.male_radio.grid(row=0, column=0, padx=10, pady=5)
        self.female_radio = ttk.Radiobutton(self.gender_frame, text="Female", variable=self.gender_var, value="female")
        self.female_radio.grid(row=0, column=1, padx=10, pady=5)

        self.button_frame = ttk.Frame(self.main_frame)
        self.button_frame.grid(row=11, column=1, padx=5, pady=5)
        self.button_frame.columnconfigure((0, 1), weight=1)

        self.register_button = ttk.Button(self.button_frame, text="Register", command=self.register)
        self.register_button.grid(row=0, column=0, pady=5)

        self.back_button = ttk.Button(self.button_frame, text="Back", command=self.create_home_page)
        self.back_button.grid(row=0, column=1, pady=5)

    def register(self):
        register_data = {
            'login': self.login_entry.get(),
            'password': self.password_entry.get(),
            'branchName': self.branch_combobox.get(),
            'firstName': self.first_name_entry.get(),
            'secondName': self.second_name_entry.get(),
            'lastName': self.last_name_entry.get(),
            'email': self.email_entry.get(),
            'dateOfBirth': self.date_of_birth_entry.get(),
            'placeOfBirth': self.place_of_birth_entry.get(),
            'gender': self.gender_var.get()
        }
        response = post_request('/register', register_data, {"X-TenantName": register_data['branchName']})

        if response.status_code == requests.codes.ok:
            print("Registration Successful")
            self.show_login_page()
        else:
            print(f"Login failed with status code {response.status_code}")
            tk.messagebox.showerror("Registration Failed", "Invalid credentials")

    def show_main_page(self):
        self.clear_main_frame()
        self.main_frame.grid(row=0, column=0, padx=50, pady=50, sticky="nsew")
        self.main_frame.columnconfigure(0, weight=1)
        self.main_frame.rowconfigure(0, weight=1)

        self.notebook = ttk.Notebook(self.main_frame)
        self.notebook.grid(row=0, column=0, padx=20, pady=20, sticky="nsew")

        # profile
        self.profile_frame = ttk.Frame(self.notebook)

        response = get_request("/user/me")
        user_data = response.json()

        self.first_name_label = ttk.Label(self.profile_frame, text=f"First name: {user_data['firstName']}")
        self.first_name_label.pack(padx=10, pady=5, anchor=tk.W)
        self.second_name_label = ttk.Label(self.profile_frame, text=f"Second name: {user_data['secondName']}")
        self.second_name_label.pack(padx=10, pady=5, anchor=tk.W)
        self.last_name_label = ttk.Label(self.profile_frame, text=f"Last name: {user_data['lastName']}")
        self.last_name_label.pack(padx=10, pady=5, anchor=tk.W)
        self.login_label = ttk.Label(self.profile_frame, text=f"Login: {user_data['login']}")
        self.login_label.pack(padx=10, pady=5, anchor=tk.W)
        self.email_label = ttk.Label(self.profile_frame, text=f"Email: {user_data['email']}")
        self.email_label.pack(padx=10, pady=5, anchor=tk.W)
        self.date_of_birth_label = ttk.Label(self.profile_frame, text=f"Date of birth: {user_data['dateOfBirth']}")
        self.date_of_birth_label.pack(padx=10, pady=5, anchor=tk.W)
        self.place_of_birth_label = ttk.Label(self.profile_frame, text=f"Place of birth: {user_data['placeOfBirth']}")
        self.place_of_birth_label.pack(padx=10, pady=5, anchor=tk.W)
        self.gender_label = ttk.Label(self.profile_frame, text=f"Gender: {user_data['gender']}")
        self.gender_label.pack(padx=10, pady=5, anchor=tk.W)

        self.notebook.add(self.profile_frame, text="Profile")

        # accounts
        self.accounts_frame = ttk.Frame(self.notebook)
        self.notebook.add(self.accounts_frame, text="Accounts")

        # transactions
        self.transactions_frame = ttk.Frame(self.notebook)
        self.notebook.add(self.transactions_frame, text="Transactions")

        self.logout_button = ttk.Button(self.main_frame, text="Logout", command=self.create_home_page)
        self.logout_button.grid(row=1, column=0, pady=5)


    def clear_main_frame(self):
        for widget in self.main_frame.winfo_children():
            widget.destroy()


# Create and run the application
app = Application()
app.mainloop()