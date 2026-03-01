package com.foodwagon.backend.controller;

import com.foodwagon.backend.dto.restaurant.RestaurantResponse;
import com.foodwagon.backend.dto.restaurant.SearchResponse;
import com.foodwagon.backend.entity.MenuItem;
import com.foodwagon.backend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor

public class RestaurantController {

    private final RestaurantService restaurantService;

    /* -------- GET RESTAURANTS -------- */
    @GetMapping
    public List<RestaurantResponse> getRestaurants(
            @RequestParam(required = false) String city
    ) {
        return restaurantService.getRestaurants(city);
    }

    /* -------- GET MENU -------- */
    @GetMapping("/{restaurantId}/menu")
    public List<MenuItem> getMenu(@PathVariable Long restaurantId) {
        return restaurantService.getMenu(restaurantId);
    }


    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Boolean> toggleStatus(@PathVariable Long id) {

        boolean status = restaurantService.changeStatus(id);

        return ResponseEntity.ok(status);
    }


    /* -------- SEARCH -------- */
    @GetMapping("/search")
    public SearchResponse search(@RequestParam String q) {
        return restaurantService.search(q);
    }
}
