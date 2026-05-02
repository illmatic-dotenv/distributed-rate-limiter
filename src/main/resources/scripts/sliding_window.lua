local windowMs = tonumber(ARGV[2]) * 1000
local now = tonumber(ARGV[3])
local windowStart = now - windowMs
redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, windowStart)
local count = redis.call('ZCARD', KEYS[1])
if count >= tonumber(ARGV[1]) then
    return -1
end
redis.call('ZADD', KEYS[1], now, now)
redis.call('EXPIRE', KEYS[1], tonumber(ARGV[2]) + 1)
return tonumber(ARGV[1]) - count - 1