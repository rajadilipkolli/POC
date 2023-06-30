/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.repository;

import com.example.poc.webmvc.entities.Post;
import java.util.Optional;

public interface CustomizedPostRepository {

    Optional<Post> findByDetailsCreatedByAndTitle(String userName, String title);
}
