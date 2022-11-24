package org.rinatzzak.dao;

import org.rinatzzak.entity.enums.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentDao extends JpaRepository<BinaryContent, Long> {
}
