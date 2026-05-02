import redis
import time
import os

r = redis.Redis(host='localhost', port=6379, decode_responses=True)

def clear():
    os.system('cls' if os.name == 'nt' else 'clear')

def get_all_keys():
    return r.keys("rl:*")

def display_dashboard():
    while True:
        clear()
        print("=" * 50)
        print("   RATE LIMITER DASHBOARD - Live Request Counts")
        print("=" * 50)
        
        keys = get_all_keys()
        
        if not keys:
            print("\n  No active rate limit keys found.")
            print("  Send some requests to see data here!")
        else:
            print(f"\n  {'CLIENT':<30} {'COUNT':<10} {'TTL'}")
            print("  " + "-" * 45)
            for key in sorted(keys):
                ttl = r.ttl(key)
                key_type = r.type(key)
                
                if key_type == 'string':
                    count = r.get(key)
                elif key_type == 'zset':
                    count = r.zcard(key)
                elif key_type == 'hash':
                    tokens = r.hget(key, 'tokens')
                    count = f"{float(tokens):.1f} tokens" if tokens else "N/A"
                else:
                    count = "N/A"
                    
                print(f"  {key:<30} {str(count):<10} {ttl}s remaining")
        
        print("\n" + "=" * 50)
        print("  Refreshing every 2 seconds. Ctrl+C to exit.")
        print("=" * 50)
        time.sleep(2)

if __name__ == "__main__":
    try:
        display_dashboard()
    except KeyboardInterrupt:
        print("\n\nDashboard stopped.")