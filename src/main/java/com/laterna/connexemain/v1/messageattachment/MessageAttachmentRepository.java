package com.laterna.connexemain.v1.messageattachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, Long> {
}
