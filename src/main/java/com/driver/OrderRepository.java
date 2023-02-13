package com.driver;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Repository
public class OrderRepository {

    HashMap<String, Order> ordersMap;
    HashMap<String, DeliveryPartner> partnersMap;
    HashMap<String, HashSet<String>> partnerOrdersMap;
    List<String> allOrders;
    HashMap<String, String> orderPartnerMap;

    public OrderRepository() {
        this.ordersMap = new HashMap<>();
        this.partnersMap = new HashMap<>();
        this.partnerOrdersMap = new HashMap<>();
        this.allOrders = new ArrayList<>();
        this.orderPartnerMap = new HashMap<>();
    }

    public void addOrder(Order order) {
        ordersMap.put(order.getId(), order);
    }

    public void addPartner(@PathVariable String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnersMap.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {

        if (ordersMap.containsKey(orderId) && partnersMap.containsKey(partnerId)) {
            HashSet<String> orderList = new HashSet<>();
            if (partnerOrdersMap.containsKey(partnerId)) {
                orderList = partnerOrdersMap.get(partnerId);
            }
            orderList.add(orderId);
            partnerOrdersMap.put(partnerId, orderList);
            partnersMap.get(partnerId).setNumberOfOrders(orderList.size());
            orderPartnerMap.put(orderId, partnerId);
        }
    }

    public Order getOrderById(@PathVariable String orderId) {

//        if (ordersMap.containsKey(orderId)) {
            Order order = ordersMap.get(orderId);
            //order should be returned with an orderId.
            return order;
//        }
    }

    public DeliveryPartner getPartnerById(String partnerId) {

//        if (partnersMap.containsKey(partnerId)) {
            DeliveryPartner deliveryPartner = partnersMap.get(partnerId);
            //deliveryPartner should contain the value given by partnerId
            return deliveryPartner;
//        }

    }

    public Integer getOrderCountByPartnerId(String partnerId) {

        Integer orderCount = 0;
        if (partnerOrdersMap.containsKey(partnerId)) {
            orderCount = partnerOrdersMap.get(partnerId).size();
        }
        //orderCount should denote the orders given by a partner-id
        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {

        HashSet<String> orders = partnerOrdersMap.get(partnerId);

        //orders should contain a list of orders by PartnerId

        return new ArrayList<>(orders);
    }

    public List<String> getAllOrders() {
        if (allOrders.isEmpty()) return new ArrayList<>();

        return allOrders;
    }

    public Integer getCountOfUnassignedOrders(){
        int totalCount = ordersMap.size();
        int assignedOrders = orderPartnerMap.size();

        return totalCount - assignedOrders;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        int undelivered = 0;

        String[] arr = time.split(":");
        String hour = arr[0];
        String minutes = arr[1];

        int givenTime = Integer.parseInt(hour) * 60 + Integer.parseInt(minutes);

        HashSet<String> orders = partnerOrdersMap.get(partnerId);

        for (String id : orders) {
            Order order = ordersMap.get(id);
            if (order.getDeliveryTime() > givenTime) undelivered++;
        }

        return undelivered;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){
        String time = "";

            HashSet<String> orders = partnerOrdersMap.get(partnerId);
            int lastDelivery = 0;

            for (String id : orders) {
                Order order = ordersMap.get(id);
                lastDelivery = Math.max(lastDelivery, order.getDeliveryTime());
            }
            //Return the time when that partnerId will deliver his last delivery order.
            int hour = lastDelivery / 60;
            int minutes = lastDelivery % 60;

            if (hour < 10) time += "0" + hour;
            else time += "" + hour;
            time += ":";
            if (minutes == 0) time += "00";
            else
                time += "" + minutes;

        return time;
    }

    public void deletePartnerById(String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        if (!partnersMap.containsKey(partnerId)) return;

        partnersMap.remove(partnerId);
        HashSet<String> orders = partnerOrdersMap.get(partnerId);
        partnerOrdersMap.remove(partnerId);
        for (String id : orders) {
            orderPartnerMap.remove(id);
        }
    }

    public void deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        if (!ordersMap.containsKey(orderId)) return;

        ordersMap.remove(orderId);
        orderPartnerMap.remove(orderId);

        for (String id : partnerOrdersMap.keySet()) {
            if (partnerOrdersMap.get(id).contains(orderId)) {
                HashSet<String> list = partnerOrdersMap.get(id);
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
