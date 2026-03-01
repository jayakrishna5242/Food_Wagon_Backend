package com.foodwagon.backend.service;

import com.foodwagon.backend.dto.restaurant.RestaurantResponse;
import com.foodwagon.backend.dto.restaurant.SearchItemResponse;
import com.foodwagon.backend.dto.restaurant.SearchResponse;
import com.foodwagon.backend.entity.MenuItem;
import com.foodwagon.backend.entity.Restaurant;
import com.foodwagon.backend.repository.MenuItemRepository;
import com.foodwagon.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepo;
    private final MenuItemRepository menuRepo;

    /* -------- GET RESTAURANTS (Public Catalog) -------- */
    public List<RestaurantResponse> getRestaurants(String city) {

        List<Restaurant> restaurants =
                (city == null || city.isBlank())
                        ? restaurantRepo.findAll().stream().filter(i->i.getOnline()).toList()
                        : restaurantRepo.findByCityIgnoreCase(city).stream().filter(i->i.getOnline()).toList();

        return restaurants.stream()
                .map(this::toRestaurantResponse)
                .toList();
    }

    /* -------- GET MENU FOR RESTAURANT -------- */
    public List<MenuItem> getMenu(Long restaurantId) {
        return menuRepo.findByRestaurantId(restaurantId).stream().filter(i->i.getInStock()).toList();
    }

    /* -------- SEARCH (Restaurants + FULL MenuItems) -------- */
    public SearchResponse search(String q) {

        // Restaurants (already correct)
        List<RestaurantResponse> restaurants =
                restaurantRepo.findByNameContainingIgnoreCase(q)
                        .stream()
                        .map(this::toRestaurantResponse)
                        .toList();

        // Menu items + restaurant name
        List<SearchItemResponse> items =
                menuRepo.findByNameContainingIgnoreCase(q)
                        .stream()
                        .filter(i->i.getInStock())
                        .map(item -> {
                            Restaurant restaurant = restaurantRepo
                                    .findById(item.getRestaurantId())
                                    .orElseThrow();

                            return new SearchItemResponse(
                                    item.getId(),
                                    item.getName(),
                                    item.getDescription(),
                                    item.getPrice(),
                                    item.getImageUrl(),
                                    item.getIsVeg(),
                                    item.getCategory(),
                                    item.getRestaurantId(),
                                    restaurant.getName(),
                                    item.getInStock()
                            );
                        })
                        .toList();

        return new SearchResponse(restaurants, items);
    }



    /* -------- ENTITY → DTO MAPPER -------- */
    private RestaurantResponse toRestaurantResponse(Restaurant r) {
        return new RestaurantResponse(
                r.getId(),
                r.getName(),
                r.getImageUrl(),
                r.getRating(),
                r.getDeliveryTime(),
                formatCostForTwo(r.getCostForTwo()),
                splitCuisines(r.getCuisines()),
                r.getLocation(),
                r.getCity()
        );
    }

    /* -------- HELPERS (NULL SAFE) -------- */
    private List<String> splitCuisines(String cuisines) {
        if (cuisines == null || cuisines.isBlank()) {
            return List.of();
        }
        return List.of(cuisines.split("\\s*,\\s*"));
    }

    private String formatCostForTwo(String cost) {
        if (cost == null || cost.isBlank()) {
            return "₹0 for two";
        }
        return cost.startsWith("₹")
                ? cost
                : "₹" + cost + " for two";
    }

    public boolean changeStatus(Long restaurantId) {

        Restaurant restaurant = restaurantRepo
                .findById(restaurantId)
                .orElse(null);

        if (restaurant == null) {
            return false;
        }

        boolean newStatus = !Boolean.TRUE.equals(restaurant.getOnline());
        restaurant.setOnline(newStatus);

        restaurantRepo.save(restaurant);

        return newStatus;
    }
}
