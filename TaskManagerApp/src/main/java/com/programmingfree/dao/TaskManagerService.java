package com.programmingfree.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.programmingfree.springservice.domain.Task;
import com.programmingfree.springservice.utility.DBUtility;

public class TaskManagerService {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(TaskManagerService.class);
 private Connection connection;

 public TaskManagerService() {
  connection = DBUtility.getConnection();
 }

 public void addTask(Task task) {
  try {
   PreparedStatement preparedStatement = connection
     .prepareStatement("insert into task_list(task_name,task_description,task_priority,task_status,task_archived,task_start_time,task_end_time) values (?, ?, ?,?,?,?,?)");
   System.out.println("Task:"+task.getTaskName());
   preparedStatement.setString(1, task.getTaskName());
   preparedStatement.setString(2, task.getTaskDescription());   
   preparedStatement.setString(3, task.getTaskPriority());
   preparedStatement.setString(4, task.getTaskStatus());
   preparedStatement.setInt(5,0);
   Date dt = new Date();

   SimpleDateFormat sdf = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   String currentTime = sdf.format(dt);
   preparedStatement.setString(6,currentTime);
   preparedStatement.setString(7,currentTime);
   preparedStatement.executeUpdate();

  } catch (SQLException e) {
   e.printStackTrace();
  }
 }
 
 public void archiveTask(int taskId) {
  try {
   PreparedStatement preparedStatement = connection
     .prepareStatement("update task_list set task_archived=true where task_id=?");
   // Parameters start with 1
   preparedStatement.setInt(1, taskId);
   preparedStatement.executeUpdate();

  } catch (SQLException e) {
   e.printStackTrace();
  }
 }
 
 public void updateTask(Task task) throws ParseException {
  try {
   PreparedStatement preparedStatement = connection
     .prepareStatement("update task_list set task_name=?, task_description=?, task_priority=?,task_status=?" +
       "where task_id=?");
   preparedStatement.setString(1, task.getTaskName());
   preparedStatement.setString(2, task.getTaskDescription());
   
   preparedStatement.setString(3, task.getTaskPriority());
   preparedStatement.setString(4, task.getTaskStatus());
   preparedStatement.setInt(4, task.getTaskId());
   preparedStatement.executeUpdate();

  } catch (SQLException e) {
   e.printStackTrace();
  }
 }
 
 public void changeTaskStatus(int taskId,String status) throws ParseException {
   try {
    PreparedStatement preparedStatement = connection
      .prepareStatement("update task_list set task_status=? where task_id=?");
    preparedStatement.setString(1,status);
    preparedStatement.setInt(2, taskId);
    preparedStatement.executeUpdate();

   } catch (SQLException e) {
    e.printStackTrace();
   }
  }


 public List<Task> getAllTasks() {
  List<Task> tasks = new ArrayList<Task>();
  try {
   Statement statement = connection.createStatement();
   ResultSet rs = statement.executeQuery("select * from task_list where task_archived=0");
   while (rs.next()) {
    Task task = new Task();
    task.setTaskId(rs.getInt("task_id"));
    task.setTaskName(rs.getString("task_name"));
    task.setTaskDescription(rs.getString("task_description"));    
    task.setTaskPriority(rs.getString("task_priority"));
    task.setTaskStatus(rs.getString("task_status"));
    tasks.add(task);
   }
  } catch (SQLException e) {
   e.printStackTrace();
  }

  return tasks;
 }
 
 public Task getTaskById(int taskId) {
  Task task = new Task();
  try {
   PreparedStatement preparedStatement = connection.
     prepareStatement("select * from task_list where task_id=?");
   preparedStatement.setInt(1, taskId);
   ResultSet rs = preparedStatement.executeQuery();
   
   if (rs.next()) {
     task.setTaskId(rs.getInt("task_id"));
     task.setTaskName(rs.getString("task_name"));
     task.setTaskDescription(rs.getString("task_description"));    
     task.setTaskPriority(rs.getString("task_priority"));
     task.setTaskStatus(rs.getString("task_status"));
   }
  } catch (SQLException e) {
   e.printStackTrace();
  }

  return task;
 }
 
}