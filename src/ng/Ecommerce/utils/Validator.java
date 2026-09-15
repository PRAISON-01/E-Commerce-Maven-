package ng.Ecommerce.utils;

import ng.Ecommerce.data.models.StoreKeeper;
import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.requests.UpdateProductRequest;
import ng.Ecommerce.exceptions.InvalidProductDataException;

import java.math.BigDecimal;

public class Validator {

    private static int MIN_STRING_LENGTH = 3;

    public static void validate(AddProductRequest request) {
        if(request == null) throw new InvalidProductDataException("Invalid input");

        validateField(request.getName(), "Name");
        validateField(request.getDescription(), "Description");
        validateBigDecimal(request.getPrice());
        validateInt(request.getQuantity());
        validateField(request.getStorekeeperEmail(), "StoreKeeperEmail");
    }

    public static void validate(UpdateProductRequest request) {
        if(request == null) throw new InvalidProductDataException("Invalid input");

        validateField(request.getName(), "Name");
        validateField(request.getDescription(), "Description");
        validateBigDecimal(request.getPrice());
        validateInt(request.getQuantity());
        validateField(request.getStorekeeperEmail(), "StoreKeeperEmail");
    }

    private static void validateInt(int quantity) {
        if(quantity <= 0)throw new InvalidProductDataException("Invalid Product Quantity");
    }

    private static void validateBigDecimal(BigDecimal price) {
        if(price == null  || price.compareTo(BigDecimal.ZERO) <= 0)throw new InvalidProductDataException("Invalid Product Price");

    }

    private static void validateField(String field, String fieldName) {
        if(isNotValidField(field)) throw new InvalidProductDataException("Invalid Product "+fieldName);
    }


    private static boolean isNotValidField(String field) {
        return field == null || field.trim().isEmpty() || field.length() <= MIN_STRING_LENGTH;
    }




}
