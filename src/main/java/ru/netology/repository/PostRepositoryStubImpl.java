package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    private final ConcurrentMap<Long, Post> posts;
    private final AtomicLong nextId;

    public PostRepositoryStubImpl() {
        posts = new ConcurrentHashMap<>();
        nextId = new AtomicLong(0L);
    }

    public List<Post> all() {
        return List.of(posts.values().stream().filter(e -> !e.isRemoved()).toArray(Post[]::new));
    }

    public Optional<Post> getById(long id) {
        if (posts.containsKey(id)) {
            Post p = posts.get(id);
            if (p.isRemoved()) throw new NotFoundException();
            return Optional.of(posts.get(id));
        } else {
            throw new NotFoundException();
        }
    }

    public Post save(Post post) {

        if (post.isRemoved()) throw new NotFoundException();

        long id = nextId.incrementAndGet();
        post.setId(id);
        posts.put(id, post);
        return post;
    }

    public void removeById(long id) {
        posts.get(id).remove();
    }
}