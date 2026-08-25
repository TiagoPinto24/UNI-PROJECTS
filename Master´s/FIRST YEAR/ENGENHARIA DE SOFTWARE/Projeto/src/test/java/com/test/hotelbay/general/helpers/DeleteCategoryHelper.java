package com.test.hotelbay.general.helpers;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;

@Service
public class DeleteCategoryHelper {

    private final RoomRepository roomRepository;

    public DeleteCategoryHelper(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public void deleteCategory(Category category) {

        List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {
            room.getCategories().remove(category);
        }

        roomRepository.saveAll(rooms);
    }
}