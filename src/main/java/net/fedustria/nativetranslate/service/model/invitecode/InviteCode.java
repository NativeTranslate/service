package net.fedustria.nativetranslate.service.model.invitecode;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing an Invite Code.
 * This class is mapped to the "fds_invite_codes" table in the database.
 */
@Entity
@Getter
@Setter
@Table(name = "fds_invite_codes")
public class InviteCode {

    /**
     * The unique identifier for the invite code.
     * This value is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The code of the invite.
     * This value must be unique.
     */
    @Column(unique = true)
    private String code;
}