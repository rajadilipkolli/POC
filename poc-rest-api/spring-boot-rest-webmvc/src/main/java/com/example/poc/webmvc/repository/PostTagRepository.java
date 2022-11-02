/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.repository;

import com.example.poc.webmvc.entities.PostTag;
import com.example.poc.webmvc.entities.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    PostTag findByTagName(String name);
}
