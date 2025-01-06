package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Post;
import com.example.bayan.Service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bayan/post")
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;
    @PostMapping("/add")
    public ResponseEntity<?> addPost(@AuthenticationPrincipal MyUser user, @RequestBody @Valid AddPostDTO addPostDTO) {
        postService.addPost(user.getId(), addPostDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Post created successfully"));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(@AuthenticationPrincipal MyUser user) {
        List<PostDTO> postDTOList = postService.myPost(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(postDTOList);
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal MyUser user, @PathVariable Integer postId, @RequestBody @Valid AddPostDTO addPostDTO) {
        postService.update(user.getId(), postId, addPostDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Post updated successfully"));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal MyUser user, @PathVariable Integer postId) {
        postService.deletePost(postId, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Post deleted successfully"));
    }
    ///------------------
    @GetMapping("/get-all-posts")
    public ResponseEntity<?> getAllPosts(){
        return ResponseEntity.status(200).body(postService.getAllPosts());
    }

    @PostMapping("/send-ad-to-broker/{brokerId}")
    public ResponseEntity<?> sendPostForOneBroker(@AuthenticationPrincipal MyUser customer, @PathVariable Integer brokerId, @RequestBody @Valid AddPostDTO addPostDTO) {
        postService.sendPostForOnoBroker(customer.getId(), brokerId, addPostDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Post sent to broker successfully"));
    }
//____________________________________________-

    @GetMapping("/broker/posts")
    public ResponseEntity<List<PostDTO>> getPostsForBroker(@AuthenticationPrincipal MyUser broker) {
        List<PostDTO> posts = postService.getPostsForBroker(broker.getId());
        return ResponseEntity.status(200).body(posts);
    }

    // Get all posts by category
    @GetMapping("/by-category/{category}")
    public ResponseEntity<?> getAllPostsByCategory(@PathVariable String category) {
        List<Post> posts = postService.getAllPostByCategory(category);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by country of origin
    @GetMapping("/by-country/{countryOfOrigin}")
    public ResponseEntity<?> getAllPostsByCountryOfOrigin(@PathVariable String countryOfOrigin) {
        List<Post> posts = postService.getAllPostByCountryOfOrigin(countryOfOrigin);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by shipment type
    @GetMapping("/by-shipment-type/{shipmentType}")
    public ResponseEntity<?> getAllPostsByShipmentType(@PathVariable String shipmentType) {
        List<Post> posts = postService.getAllPostByShipmentType(shipmentType);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by ca tegory and country of origin
    @GetMapping("/by-category-and-country/{category}/{countryOfOrigin}")
    public ResponseEntity<?> getAllPostsByCategoryAndCountryOfOrigin(@PathVariable String category, @PathVariable String countryOfOrigin) {
        List<Post> posts = postService.getAllPostByTheCategoryAndCountryOfOrigin(category, countryOfOrigin);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by category and shipment type
    @GetMapping("/by-category-and-shipment-type/{category}/{shipmentType}")
    public ResponseEntity<?> getAllPostsByCategoryAndShipmentType(@PathVariable String category, @PathVariable String shipmentType) {
        List<Post> posts = postService.getAllPostByCategoryAndShipmentType(category, shipmentType);
        return ResponseEntity.ok(posts);
    }

    // Get all posts by category, shipment type, and country of origin
    @GetMapping("/by-category-shipment-type-country/{category}/{shipmentType}/{countryOfOrigin}")
    public ResponseEntity<?> getAllPostsByCategoryAndShipmentTypeAndCountryOfOrigin(@PathVariable String category, @PathVariable String shipmentType, @PathVariable String countryOfOrigin) {
        List<Post> posts = postService.getAllPostByCategoryAndShipmentTypeAndCountryOfOrigin(category, shipmentType, countryOfOrigin);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/general-post-statistics")
    public ResponseEntity<?> getGeneralPostStatisticsForCustomer(@AuthenticationPrincipal MyUser customer) {
        Map<String, Object> stats = postService.getGeneralPostStatisticsForCustomer(customer.getId());
        return ResponseEntity.status(200).body(stats);
    }

    @GetMapping("/offer-statistics/{postId}")
    public ResponseEntity<?> getOfferDetailsForPost(@PathVariable Integer postId,
                                                       @AuthenticationPrincipal MyUser customer) {
        Map<String, Object> stats = postService.getOfferDetailsForPost(postId, customer.getId());
        return ResponseEntity.status(200).body(stats);
    }

}
