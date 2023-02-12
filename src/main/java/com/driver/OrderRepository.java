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

    HashMap<String, Order> ordersMap;
    HashMap<String, DeliveryPartner> partnersMap;
    HashMap<String, List<String>> partnerOrdersMap;
    List<String> allOrders;
    List<String> unassignedOrders;

    public OrderRepository() {
        this.ordersMap = new HashMap<>();
        this.partnersMap = new HashMap<>();
        this.partnerOrdersMap = new HashMap<>();
        this.allOrders = new ArrayList<>();
        this.unassignedOrders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        String id = order.getId();
        allOrders.add(id);
        unassignedOrders.add(id);
        ordersMap.put(id, order);
    }

    public void addPartner(@PathVariable String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnersMap.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if (!partnerOrdersMap.containsKey(partnerId)) {
            partnerOrdersMap.put(partnerId, new ArrayList<>());
        }
        int num = partnersMap.get(partnerId).getNumberOfOrders();
        num += 1;
        partnersMap.get(partnerId).setNumberOfOrders(num);
        partnerOrdersMap.get(partnerId).add(orderId);
        unassignedOrders.remove(orderId);

    }

    public Order getOrderById(@PathVariable String orderId) {

        Order order = ordersMap.get(orderId);
        //order should be returned with an orderId.

        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId) {

        DeliveryPartner deliveryPartner = partnersMap.get(partnerId);

        //deliveryPartner should contain the value given by partnerId

        return deliveryPartner;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {

        Integer orderCount = partnerOrdersMap.get(partnerId).size();

        //orderCount should denote the orders given by a partner-id

        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {

        List<String> orders = partnerOrdersMap.get(partnerId);

        //orders should contain a list of orders by PartnerId

        return orders;
    }

    public List<String> getAllOrders() {
        if (allOrders.isEmpty()) return new ArrayList<>();

        return allOrders;
    }

    public Integer getCountOfUnassignedOrders(){
        if(unassignedOrders.isEmpty()) return 0;
        Integer countOfOrders = unassignedOrders.size();

        return countOfOrders;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        String[] arr = time.split(":");
        String hour = arr[0];
        String minutes = arr[1];
        int givenTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minutes);
        List<String> orders = partnerOrdersMap.get(partnerId);
        int undelivered = 0;

        for (String id : orders) {
            Order order = ordersMap.get(id);
            if (order.getDeliveryTime() > givenTime) undelivered++;
        }
        return undelivered;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){
        String time = "";
        List<String> orders = partnerOrdersMap.get(partnerId);
        int lastDelivery = Integer.MIN_VALUE;

        for (String id : orders) {
            Order order = ordersMap.get(id);
            if (order.getDeliveryTime() > lastDelivery) lastDelivery = order.getDeliveryTime();
        }
        //Return the time when that partnerId will deliver his last delivery order.
        int hour = lastDelivery / 60;
        int minutes = lastDelivery % 60;

        if (hour < 10) time += "0" + hour;
        else time += hour;
        time += ":";
        time += minutes;
        return time;
    }

    public void deletePartnerById(String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        if (!partnersMap.containsKey(partnerId)) return;

        partnersMap.remove(partnerId);
        List<String> orders = partnerOrdersMap.get(partnerId);
        partnerOrdersMap.remove(partnerId);
        for (String id : orders) {
            unassignedOrders.add(id);
        }
    }

    public void deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        ordersMap.remove(orderId);

        for (String id : partnerOrdersMap.keySet()) {
            if (partnerOrdersMap.get(id).contains(orderId)) {
                List<String> list = partnerOrdersMap.get(id);
                list.remove(orderId);
                partnerOrdersMap.put(id, list);
                break;
            }
        }

    }
}



//@Repository
//public class OrderRepository {
//
//    Map<String,Order> orderMap;
//    Map<String,DeliveryPartner> partnerMap;
//    Map<String, List<String>> ordersToPartnerMap;
//
//    List<String> unassigned = new ArrayList<>();
//
//    public OrderRepository() {
//        orderMap = new HashMap<>();
//        partnerMap = new HashMap<>();
//        ordersToPartnerMap = new HashMap<>();
//    }
//
//    public String addOrder(Order order){
//        String id = order.getId();
//        orderMap.put(id,order);
//        unassigned.add(id);
//        return "order added successfully" ;
//    }
//
//    public String addPartner(String partnerId) {
//        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
//        String id = deliveryPartner.getId();
//        partnerMap.put(id,deliveryPartner);
//        return "Partner added successfully";
//    }
//
//    public String addOrderPartnerPair(String orderId,String partnerId){
//        unassigned.remove(orderId);
//        if(!ordersToPartnerMap.containsKey(partnerId)){
//            ordersToPartnerMap.put(partnerId,new ArrayList<>());
//
//        }
//            List<String> order = ordersToPartnerMap.get(partnerId);
//            order.add(orderId);
//            ordersToPartnerMap.put(partnerId,order );
//
//
//            return "added order to partner";
//    }
//
//    public Order getOrderById(String orderId){
//        Order order = new Order();
//        for(String id : orderMap.keySet()) {
//            if (id.equals(orderId)) {
//                order = orderMap.get(id);
//            }
//        }
//        return order;
//    }
//
//    public DeliveryPartner getPartnerById(String partnerId){
//        return partnerMap.get(partnerId);
//    }
//
//    public int getOrderCountByPartnerId(String partnerId){
//            List<String> count = ordersToPartnerMap.get(partnerId);
//            return count.size();
//
//    }
//
//    public List<String> getOrdersByPartnerId(String partnerId){
//        List<String> order = new ArrayList<>();
//        for(String id : ordersToPartnerMap.keySet()){
//            if(id.equals(partnerId)){
//                 order = ordersToPartnerMap.get(partnerId);
//            }
//        }
//
//        return order;
//    }
//
//    public List<String> getAllOrders(){
//        List<String> allOrders = new ArrayList<>();
//        for(String order : orderMap.keySet()){
//            allOrders.add(order);
//        }
//        return allOrders;
//    }
//
//    public Integer getCountOfUnassignedOrders(){
//
//        return unassigned.size();
//    }
//
//    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
//        return 0;
//    }
//
//    public String getLastDeliveryTimeByPartnerId( String partnerId){
//        List<String> orders = ordersToPartnerMap.get(partnerId);
//        String LastOrder = orders.get(orders.size()-1);
//
//        Order order = orderMap.get(LastOrder);
//        return String.valueOf(order.getDeliveryTime());
//    }
//
//    public String deletePartnerById( String partnerId){
//        if(partnerMap.containsKey(partnerId))
//            partnerMap.remove(partnerId);
//        if(ordersToPartnerMap.containsKey(partnerId)){
//            List<String> order = ordersToPartnerMap.get(partnerId);
//
//            for(String s : order){
//                unassigned.add(s);
//            }
//        }
//        return "partner id deleted successfully";
//    }
//
//    public String deleteOrderById( String orderId){
//        orderMap.remove(orderId);
//
//        for(List<String> l : ordersToPartnerMap.values()){
//            for(String s : l){
//                if(s.equals(orderId)){
//                    l.remove(orderId);
//                }
//            }
//        }
//        return "Order id deleted successfully";
//    }
//
//}
