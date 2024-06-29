import os
import requests
import urllib3
import threading

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

users = {
    "BASE": "mail1@mail.mail",
    "STANDARD": "mail2@mail.mail",
    "GOLD": "mail3@mail.mail"
}

packages = ["BASE", "STANDARD", "GOLD"]

horizontal_dash = "_______________________________________________________________________________________________________________________________________________\n"

def print_results(responses, successful, blocked, failed):
    print(horizontal_dash)

    print("\nResponses:")
    for index, response in enumerate(responses, start=1):
        print(f"{index}.\t{response}")
    print("\n")
    
    print(f"Number of successful requests: {successful}")
    print(f"Number of blocked requests: {blocked}")
    print(f"Number of failed requests: {failed}")
    
    print(horizontal_dash)

def send_request(url, responses, successful, blocked, failed):
    try:
        response = requests.post(url, verify=False)
        if response.status_code == 200:
            responses.append(f"Response body: {response.json()}")
            successful.increment()
        elif response.status_code == 429:
            responses.append("Rate limit reached.")
            blocked.increment()
        else:
            responses.append(f"Unexpected error: {response}")
            failed.increment()
    except Exception as e:
        responses.append(f"Exception occurred: {str(e)}")
        failed.increment()

def send_requests(email, ad_id, num_visits, cert_path):
    base_url = f'https://localhost/api/advertisement/visit/{email}/{ad_id}'
    
    responses = []
    successful = Counter()
    blocked = Counter()
    failed = Counter()

    threads = []

    for _ in range(num_visits):
        url = base_url
        thread = threading.Thread(target=send_request, args=(url, responses, successful, blocked, failed))
        threads.append(thread)
        thread.start()

    for thread in threads:
        thread.join()

    print_results(responses, successful.value, blocked.value, failed.value)

class Counter:
    def __init__(self):
        self._value = 0
        self._lock = threading.Lock()

    def increment(self):
        with self._lock:
            self._value += 1

    @property
    def value(self):
        return self._value

def main():
    while True:
        print("Choose user package:")
        print("1. Base")
        print("2. Standard")
        print("3. Gold")
        
        try:
            choice = input("Enter 1, 2, or 3: ")
            package_choice = packages[int(choice) - 1]
            email = users[package_choice]
            
            ad_id = input("Enter adId: ")
            num_visits = int(input("Enter number of visits: "))

            script_dir = os.path.dirname(os.path.realpath(__file__))
            cert_path = os.path.join(script_dir, 'certificates', 'client', 'client.crt')
            
            send_requests(email, ad_id, num_visits, cert_path)
            
            while True:
                continue_choice = input("Do you want to make another request? (y/n): ").strip().lower()
                if continue_choice in ['y', 'n']:
                    break
                print("Invalid input. Please enter 'y' or 'n'.")
            
            if continue_choice == 'n':
                print("Exiting the program.")
                break
        
        except ValueError:
            print("Invalid input. Please enter 1, 2, or 3 for package choice and a valid number for visits.")
        except Exception as e:
            print(f"An error occurred: {str(e)}")

if __name__ == "__main__":
    main()
