package com.theelearninghub.managers;

import com.theelearninghub.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Course findOneByTitle(String title);

    List<Course> findByCategoryLikeAndLevelLikeAndTitleContainingAndIsPrivateFalseAndValidatedTrue(String category, String level, String title);

    List<Course> findByHighlightedAndIsPrivateFalseAndValidatedTrue(boolean highlighted);

    @Query(value = "SELECT * FROM course a WHERE a.idcourse IN " +
            "(SELECT at.course_idcourse FROM user_attends_course at WHERE at.user_iduser != :userId GROUP BY at.course_idcourse ORDER BY COUNT(DISTINCT at.user_iduser) DESC) " +
            "and a.creator_id != :userId " +
            "and a.idcourse NOT IN "+
            "(SELECT at.course_idcourse FROM user_attends_course at WHERE at.user_iduser = :userId) "+
            "and a.is_private = false " +
            "and a.validated = true " +
            "and a.highlighted=true "+
            "LIMIT 12",
            nativeQuery = true)
    List<Course> findPopularCourses(@Param("userId") int userId);

    @Query(value = "SELECT * FROM course a WHERE a.idcourse IN " +
            "(SELECT at.course_idcourse FROM user_attends_course at " +
            "JOIN course c on at.course_idcourse = c.idcourse " +
            "WHERE at.user_iduser != :userId AND c.category = :category " +
            "GROUP BY at.course_idcourse ORDER BY COUNT(at.user_iduser) DESC) " +
            "AND a.idcourse NOT IN "+
            "(SELECT at.course_idcourse FROM user_attends_course at WHERE at.user_iduser = :userId) "+
            "and a.creator_id != :userId " +
            "and a.is_private = false " +
            "and a.validated = true " +
            "LIMIT 12",
            nativeQuery = true)
    List<Course> findSugestedCourses(@Param("userId") int userId, @Param("category") String category);

    @Query(value = "SELECT c.category FROM user_attends_course at " +
            "JOIN course c on at.course_idcourse = c.idcourse " +
            "WHERE at.user_iduser = :userId " +
            "GROUP BY c.category ORDER BY COUNT(at.user_iduser) DESC " +
            "LIMIT 1",
            nativeQuery = true)
    String findMostFrequentCategory(@Param("userId") int userId);

    @Query(value = "SELECT c.category FROM user_attends_course at " +
            "JOIN course c on at.course_idcourse = c.idcourse " +
            "WHERE at.course_idcourse = :courseId " +
            "GROUP BY c.category ORDER BY COUNT(at.user_iduser) DESC " +
            "LIMIT 1",
            nativeQuery = true)
    String getPopularCategoryAmongEnrolled(@Param("courseId") int courseId);

    @Query(value = "SELECT c.category FROM course c " +
            "JOIN user_attends_course at ON c.idcourse = at.course_idcourse " +
            "JOIN user u ON at.user_iduser = u.iduser " +
            "JOIN user_has_room ur ON u.iduser = ur.user_iduser " +
            "WHERE ur.room_idroom = :roomId " +
            "GROUP BY c.category " +
            "ORDER BY COUNT(ur.user_iduser) DESC " +
            "LIMIT 1", nativeQuery = true)
    String getPopularCategoryInRoom(@Param("roomId") int roomId);
}
