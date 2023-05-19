package com.librarymanagement.library.repositories;

import com.librarymanagement.library.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution,Long> {
    @Query("SELECT te FROM TaskExecution te WHERE te.id = :id")
    TaskExecution getTaskExecution(Long id);
    @Query("SELECT te FROM TaskExecution te WHERE te.taskType=:taskExecutionType AND te.borrowedBookId=:borrowedBookId")
    TaskExecution getMatchingTaskExecution(Long borrowedBookId, TaskExecutionType taskExecutionType);
    @Query("SELECT te FROM TaskExecution te WHERE te.borrowedBookId=:borrowedBookId")
    List<TaskExecution> getAllTaskExecutionsForBorrowedBook(Long borrowedBookId);
}
