package com.driver;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String,Order> orderMap;
    Map<String,DeliveryPartner> partnerMap;
    Map<String, List<String>> ordersToPartnerMap;

    List<String> unassigned = new ArrayList<>();

    public OrderRepository() {
        orderMap = new HashMap<>();
        partnerMap = new HashMap<>();
        ordersToPartnerMap = new HashMap<>();
    }

    public String addOrder(Order order){
        String id = order.getId();
        orderMap.put(id,order);
        unassigned.add(order.getId());
        return "order added successfully";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        String id = deliveryPartner.getId();
        partnerMap.put(id,deliveryPartner);
        return "Partner added successfully";
    }

    public String addOrderPartnerPair(String orderId,String partnerId){

        if(!ordersToPartnerMap.containsKey(partnerId)){
            ordersToPartnerMap.put(partnerId,new ArrayList<>());

        }
            List<String> order = ordersToPartnerMap.get(partnerId);
            order.add(orderId);
            ordersToPartnerMap.put(partnerId,order );
            unassigned.remove(orderId);

            return "added order to partner";
    }

    public Order getOrderById(String orderId){
            return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return partnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
            List<String> count = ordersToPartnerMap.get(partnerId);
            return count.size();

    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> order = ordersToPartnerMap.get(partnerId);
        return order;
    }

    public List<String> getAllOrders(){
        List<String> allOrders = new ArrayList<>();
        for(String order : orderMap.keySet()){
            allOrders.add(order);
        }
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders(){
        return unassigned.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        return 0;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){
        List<String> orders = ordersToPartnerMap.get(partnerId);
        String LastOrder = orders.get(orders.size()-1);

        Order order = orderMap.get(LastOrder);
        return String.valueOf(order.getDeliveryTime());
    }

    public String deletePartnerById( String partnerId){
        partnerMap.remove(partnerId);
        if(ordersToPartnerMap.containsKey(partnerId)){
            List<String> order = ordersToPartnerMap.get(partnerId);

            for(String s : order){
                unassigned.add(s);
            }
        }
        return "partner id deleted successfully";
    }

    public String deleteOrderById( String orderId){
        orderMap.remove(orderId);

        for(List<String> l : ordersToPartnerMap.values()){
            for(String s : l){
                if(s.equals(orderId)){
                    l.remove(orderId);
                }
            }
        }
        return "Order id deleted successfully";
    }

}
