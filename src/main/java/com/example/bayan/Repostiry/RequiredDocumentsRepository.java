package com.example.bayan.Repostiry;


import com.example.bayan.Model.RequiredDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequiredDocumentsRepository extends JpaRepository<RequiredDocuments, Integer> {

    @Query("select d from RequiredDocuments d where d.post.id=?1")
    List<RequiredDocuments> findAllByPostId(Integer postId) ;

    RequiredDocuments findRequiredDocumentsById(Integer id);

    RequiredDocuments findRequiredDocumentsByIdAndPostId(Integer id, Integer postId);
    List<RequiredDocuments> findRequiredDocumentsByPostId(Integer postId);
}
