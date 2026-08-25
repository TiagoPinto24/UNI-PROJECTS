package com.example.sponsordonkey.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.sponsordonkey.data.Post.Post;
import com.example.sponsordonkey.data.Post.PostRepository;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }
    
    public Optional<Post> get(Long id) {
        return repository.findById(id);
    }

    public Post save(Post entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    public int count() {
        return (int) repository.count();
    }

    public List<Post> listNext50(int startingId) {
        int count = count();
        int subListSize = Math.min(startingId + 50,count);
        return repository.findAll().subList(startingId, subListSize);
    }

    
}
