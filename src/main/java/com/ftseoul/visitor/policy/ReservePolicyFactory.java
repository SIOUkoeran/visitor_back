package com.ftseoul.visitor.policy;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Component
@Transactional
public class ReservePolicyFactory {

    private final HashMap<ReserveType, ReservePolicy> reservePolicyHashMap = new HashMap<>();

    public ReservePolicyFactory(List<ReservePolicy> reservePolicyList) {
        if (reservePolicyList.isEmpty()){
            throw new IllegalArgumentException("대표");
        }
        for (ReservePolicy reservePolicy : reservePolicyList){
            reservePolicyHashMap.put(reservePolicy.getType(), reservePolicy);
        }
    }

    public ReservePolicy getPolicy(ReserveType reserveType){
        return reservePolicyHashMap.get(reserveType);
    }
}
