local capacity = tonumber(ARGV[1])
local refillRate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local bucket = redis.call('HMGET', KEYS[1], 'tokens', 'last_refill')
local tokens = tonumber(bucket[1]) or capacity
local lastRefill = tonumber(bucket[2]) or now
local elapsed = (now - lastRefill) / 1000.0
local newTokens = math.min(capacity, tokens + (elapsed * refillRate))
if newTokens < 1 then
    return -1
end
newTokens = newTokens - 1
redis.call('HMSET', KEYS[1], 'tokens', newTokens, 'last_refill', now)
redis.call('EXPIRE', KEYS[1], 3600)
return math.floor(newTokens)