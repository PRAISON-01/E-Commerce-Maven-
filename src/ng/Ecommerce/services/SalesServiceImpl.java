package ng.Ecommerce.services;

import ng.Ecommerce.dtos.requests.CreateOrderRequest;
import ng.Ecommerce.dtos.responses.CreateOrderResponse;
import ng.Ecommerce.exceptions.InvalidProductDataException;

public class SalesServiceImpl implements SalesService{
    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) throws InvalidProductDataException {
        return null;
    }
}
