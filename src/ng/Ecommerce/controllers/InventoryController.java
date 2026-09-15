package ng.Ecommerce.controllers;

import ng.Ecommerce.dtos.requests.AddProductRequest;
import ng.Ecommerce.dtos.requests.DeleteProductRequest;
import ng.Ecommerce.dtos.requests.UpdateProductRequest;
import ng.Ecommerce.dtos.responses.AddProductResponse;
import ng.Ecommerce.dtos.responses.ApiResponse;
import ng.Ecommerce.dtos.responses.DeleteProductResponse;
import ng.Ecommerce.dtos.responses.UpdateProductResponse;
import ng.Ecommerce.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest request) {
        try{
            AddProductResponse response = inventoryService.addProduct((request));
            return new ResponseEntity<>(new ApiResponse( true , response), CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }


    @PostMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductRequest request) {
        try{
            UpdateProductResponse response = inventoryService.updateProduct((request));
            return new ResponseEntity<>(new ApiResponse( true , response), OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> updateProduct(@RequestBody DeleteProductRequest request) {
        try{
            DeleteProductResponse response = inventoryService.deleteProduct((request));
            return new ResponseEntity<>(new ApiResponse( true , response), ACCEPTED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

}
