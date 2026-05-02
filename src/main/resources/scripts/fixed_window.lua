local current = redis.call('GET', KEYS[1])
if current == false then
    redis.call('SET', KEYS[1], 1, 'EX', tonumber(ARGV[2]))
    return tonumber(ARGV[1]) - 1
end
if tonumber(current) >= tonumber(ARGV[1]) then
    return -1
end
redis.call('INCR', KEYS[1])
return tonumber(ARGV[1]) - tonumber(current) - 1