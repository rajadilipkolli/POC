/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.repository;

import com.example.poc.webmvc.entities.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
}
