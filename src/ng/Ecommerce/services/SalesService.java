package ng.Ecommerce.services;

import ng.Ecommerce.dtos.responses.CreateOrderResponse;
import ng.Ecommerce.dtos.requests.CreateOrderRequest;
import ng.Ecommerce.exceptions.InvalidProductDataException;

public interface SalesService {

    CreateOrderResponse createOrder (CreateOrderRequest createOrderRequest) throws InvalidProductDataException;
//    void listOrders();
//    void cancelOrder();
//    void updateStatus();

}
