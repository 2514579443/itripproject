package cn.itrip.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisAPI {

    public JedisPool jedisPool;
    public JedisPool getJedisPool(){
        return  jedisPool;
    }
    public void setJedisPool(JedisPool jedisPool){
        this.jedisPool=jedisPool;
    }

    public boolean set(String key,String value){
        try {
            Jedis jedis=jedisPool.getResource();
            jedis.set(key,value);
            return  true;

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public boolean set(String key,int seconds,String value){
        try {
            Jedis jedis=jedisPool.getResource();
            jedis.setex(key,seconds,value);
            return  true;
        }catch (Exception e){
            e.printStackTrace();;
        }
        return  false;
    }

    public boolean exist(String key){
        try {
            Jedis jedis=jedisPool.getResource();
            return jedis.exists(key);
        }catch (Exception e){
            e.printStackTrace();;
        }
        return  false;
    }

    public static  void returnResource(JedisPool pool,Jedis redis){
        if (redis !=null){
            pool.returnResource(redis);
        }
    }

    public String get(String key){
        String value=null;
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            value=jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedisPool ,jedis);
        }
        return value;
    }
    public Long ttl(String key){
        try {
            Jedis jedis=jedisPool.getResource();
            return jedis.ttl(key);

        }catch (Exception e){
            e.printStackTrace();;
        }
        return (long)-2;
    }

    public void delete(String key){
        try {
            Jedis jedis=jedisPool.getResource();
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();;
        }
    }
}
