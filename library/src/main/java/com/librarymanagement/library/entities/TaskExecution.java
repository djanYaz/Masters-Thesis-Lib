package com.librarymanagement.library.entities;

import javax.persistence.*;

@Entity
@Table(name="taskexecution")
public class TaskExecution {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    public TaskExecutionType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskExecutionType taskType) {
        this.taskType = taskType;
    }

    @Column(name="task_type")
    @Enumerated(EnumType.ORDINAL)
    private TaskExecutionType taskType;

    @Column(name = "borrowedbook_id")
    Long borrowedBookId;


    public TaskExecution() {
    }

    public TaskExecution(TaskExecutionType taskType, Long borrowedBookId) {
        this.taskType = taskType;
        this.borrowedBookId = borrowedBookId;
    }
}
