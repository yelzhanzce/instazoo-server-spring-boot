package com.yelzhan.demo.web;

import com.yelzhan.demo.entity.ImageModel;
import com.yelzhan.demo.payload.response.MessageResponse;
import com.yelzhan.demo.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageUploadController {
    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);

        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@RequestParam("file") MultipartFile file,
                                                             @PathVariable("postId") String postId,
                                                             Principal principal) throws IOException{
        imageUploadService.uploadImageToPost(file, principal, Long.parseLong(postId));

        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageForUser(Principal principal){
        ImageModel imageModel = imageUploadService.getImageToUser(principal);
        return new ResponseEntity<>(imageModel, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModel> getImageForPost(@PathVariable("postId") String postId){
        ImageModel imageModel = imageUploadService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(imageModel, HttpStatus.OK);
    }
}
