package com.brain.neuron.util.imageloader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class CancelableRequestDelegate {
    private Map<Integer, String> mCheckUnRealRequest = Collections
            .synchronizedMap(new HashMap<Integer, String>());


    public void putRequest(int hashCode, String cacheKey) {
        mCheckUnRealRequest.put(hashCode, cacheKey);
    }

    public String getCacheKey(int hashCode) {
        return mCheckUnRealRequest.get(hashCode);
    }

    public void remove(int hashCode) {
        mCheckUnRealRequest.remove(hashCode);
    }


}
