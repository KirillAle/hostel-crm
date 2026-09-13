package com.hostel.crm.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "guest")
class Guest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "full_name", nullable = false, length = 255)
    var fullName: String,

    @Column(name = "passport", nullable = false, unique = true, length = 50)
    var passport: String,

    @Column(name = "photo", length = 500)
    var photo: String?,

    @Column(name = "birth_date", nullable = false)
    var birthDate: LocalDate,

    @Column(name = "check_in_date")
    var checkInDate: LocalDate? = null,

    @Column(name = "check_out_date")
    var checkOutDate: LocalDate? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    var apartment: Apartment? = null
)