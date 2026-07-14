import urllib.request
import json
import random
import time
import concurrent.futures

BASE_URL = "http://localhost:8085"

# Helper function to send HTTP requests using urllib
def send_request(path, method="GET", headers=None, data=None):
    url = f"{BASE_URL}{path}"
    req_headers = {"Content-Type": "application/json"}
    if headers:
        req_headers.update(headers)
    
    req_data = None
    if data:
        req_data = json.dumps(data).encode("utf-8")
        
    req = urllib.request.Request(url, data=req_data, headers=req_headers, method=method)
    
    try:
        with urllib.request.urlopen(req) as response:
            res_data = response.read().decode("utf-8")
            if response.status >= 200 and response.status < 300:
                return json.loads(res_data) if res_data else {}, response.status
    except urllib.error.HTTPError as e:
        err_data = e.read().decode("utf-8")
        try:
            return json.loads(err_data), e.code
        except Exception:
            return {"error": e.reason, "message": err_data}, e.code
    except Exception as e:
        return {"error": "ConnectionError", "message": str(e)}, 500

# Simulated Client class
class BankClient:
    def __init__(self, username, email, password, first_name, last_name):
        self.username = username
        self.email = email
        self.password = password
        self.first_name = first_name
        self.last_name = last_name
        self.token = None
        self.accounts = []
        self.loans = []

    def register(self):
        payload = {
            "userId": None,
            "firstName": self.first_name,
            "lastName": self.last_name,
            "email": self.email,
            "userName": self.username,
            "password": self.password,
            "accountIds": [],
            "loanIds": []
        }
        res, status = send_request("/auth/register", "POST", data=payload)
        return status == 200

    def login(self):
        payload = {
            "username": self.email,
            "password": self.password
        }
        res, status = send_request("/auth/login", "POST", data=payload)
        if status == 200:
            self.token = res.get("token")
            return True
        return False

    def create_account(self, account_type, initial_amount):
        headers = {"Authorization": f"Bearer {self.token}"}
        payload = {
            "id": None,
            "sourceAmount": initial_amount,
            "sourceAmountType": account_type,
            "sourceRoutingNumber": "123456789",
            "sourceAmountNumber": f"ACT-{self.username}-{random.randint(1000, 9999)}"
        }
        res, status = send_request("/api/accounts", "POST", headers=headers, data=payload)
        if status == 200:
            self.accounts.append(res)
            return res
        return None

    def get_accounts(self):
        headers = {"Authorization": f"Bearer {self.token}"}
        res, status = send_request("/api/accounts", "GET", headers=headers)
        if status == 200:
            return res
        return []

    def apply_loan(self, amount, term, rate):
        headers = {"Authorization": f"Bearer {self.token}"}
        payload = {
            "loanId": None,
            "amount": amount,
            "termInMonths": term,
            "interestRate": rate,
            "loanType": "PERSONAL",
            "status": "APPLIED",
            "loanPaymentSchedule": "MONTHLY"
        }
        res, status = send_request("/api/loans", "POST", headers=headers, data=payload)
        if status == 200:
            self.loans.append(res)
            return res
        return None

    def transfer(self, from_account_id, to_account_id, amount):
        headers = {"Authorization": f"Bearer {self.token}"}
        payload = {
            "TransferId": random.randint(100000, 999999),
            "fromAccountId": from_account_id,
            "toAccountId": to_account_id,
            "transferAmount": amount,
            "transferType": "ACCOUNTtoACCOUNTTRANSFER"
        }
        res, status = send_request("/api/transfer", "POST", headers=headers, data=payload)
        return res, status


def run_simulation():
    print("====================================================")
    print("      ENTERPRISE BANK SIMULATOR (PYTHON ENGINE)     ")
    print("====================================================\n")

    # Generate 5 clients
    clients = [
        BankClient("alice", "alice@bank.com", "alicepass123", "Alice", "Smith"),
        BankClient("bob", "bob@bank.com", "bobpass123", "Bob", "Jones"),
        BankClient("charlie", "charlie@bank.com", "charliepass123", "Charlie", "Brown"),
        BankClient("david", "david@bank.com", "davidpass123", "David", "Miller"),
        BankClient("eve", "eve@bank.com", "evepass123", "Eve", "Wilson")
    ]

    print("[Step 1] Registering and authenticating clients concurrently...")
    with concurrent.futures.ThreadPoolExecutor(max_workers=5) as executor:
        # Register all
        reg_futures = {executor.submit(c.register): c for c in clients}
        for future in concurrent.futures.as_completed(reg_futures):
            c = reg_futures[future]
            try:
                success = future.result()
                if success:
                    print(f"  + Registered {c.first_name} {c.last_name}")
                else:
                    print(f"  x Failed to register {c.first_name}")
            except Exception as e:
                print(f"  x Error registering {c.first_name}: {e}")

        # Login all
        login_futures = {executor.submit(c.login): c for c in clients}
        for future in concurrent.futures.as_completed(login_futures):
            c = login_futures[future]
            try:
                success = future.result()
                if success:
                    print(f"  * Authenticated {c.first_name} (Token acquired)")
                else:
                    print(f"  x Login failed for {c.first_name}")
            except Exception as e:
                print(f"  x Login error for {c.first_name}: {e}")

    print("\n[Step 2] Opening accounts and depositing initial balances...")
    for c in clients:
        if c.token:
            # Create checking account with $1000
            checking = c.create_account("CHECKING", 1000.0)
            if checking:
                print(f"  + Created CHECKING account for {c.first_name}: ID={checking.get('accountId')}, Balance=${checking.get('balance')}")
            
            # Create savings account with $500
            savings = c.create_account("SAVINGS", 500.0)
            if savings:
                print(f"  + Created SAVINGS account for {c.first_name}: ID={savings.get('accountId')}, Balance=${savings.get('balance')}")

    # Gather all account IDs
    all_account_ids = []
    account_owner_map = {}
    for c in clients:
        if c.token:
            user_accounts = c.get_accounts()
            for acc in user_accounts:
                acc_id = acc.get("id")
                all_account_ids.append(acc_id)
                account_owner_map[acc_id] = c

    print(f"\nCollected {len(all_account_ids)} accounts across all clients: {all_account_ids}")

    print("\n[Step 3] Running concurrent transaction stress-test (50 transfers)...")
    first_transfer_printed = [False]
    success_count = 0
    failure_count = 0

    def execute_random_transfer():
        if len(all_account_ids) < 2:
            return None, 500
        
        # Pick random source and destination
        from_id = random.choice(all_account_ids)
        to_id = random.choice(all_account_ids)
        while from_id == to_id:
            to_id = random.choice(all_account_ids)
            
        owner = account_owner_map[from_id]
        amount = round(random.uniform(5.0, 150.0), 2)
        
        res, status = owner.transfer(from_id, to_id, amount)
        if not first_transfer_printed[0]:
            first_transfer_printed[0] = True
            print(f"  -> Transfer sample: ${amount} from Account ID {from_id} to {to_id}")
            print(f"  -> Server Response: {res}")
        return {
            "from": from_id,
            "to": to_id,
            "amount": amount,
            "status": res.get("status") if status == 200 else "FAILED",
            "message": res.get("message")
        }, status

    start_time = time.time()
    with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
        transfer_futures = [executor.submit(execute_random_transfer) for _ in range(50)]
        for future in concurrent.futures.as_completed(transfer_futures):
            try:
                res, status = future.result()
                if status == 200 and res.get("status") == "QUEUED":
                    success_count += 1
                else:
                    failure_count += 1
            except Exception as e:
                failure_count += 1

    end_time = time.time()
    execution_time = end_time - start_time

    print(f"\n[Step 4] Applying for loans for clients...")
    for c in clients:
        if c.token:
            loan = c.apply_loan(10000.0, 12, 4.5)
            if loan:
                print(f"  + Applied & Approved Personal Loan for {c.first_name}: ID={loan.get('loanId')}, Amount=${loan.get('approvedAmount')}")

    print("\n====================================================")
    print("                 SIMULATION REPORT                  ")
    print("====================================================")
    print(f"Total Transactions Simulated:  50")
    print(f"Successfully Queued:          {success_count}")
    print(f"Failed (e.g. invalid bounds): {failure_count}")
    print(f"Concurrent Execution Time:     {execution_time:.3f} seconds")
    print(f"Throughput:                    {50/execution_time:.1f} req/sec")
    print("====================================================\n")

if __name__ == "__main__":
    # Note: Make sure the Spring Boot backend is running on localhost:8080 before executing
    try:
        run_simulation()
    except Exception as e:
        print(f"Error running simulator: {e}")
        print("Please ensure the Spring Boot server is running on http://localhost:8080")
