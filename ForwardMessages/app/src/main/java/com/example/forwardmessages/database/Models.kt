package com.example.forwardmessages

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Entity(tableName = "groups_table")
data class Group(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNumber: String?,
    val contentText: String?,
    val enabled: Boolean,
)

@Parcelize
@Entity(tableName = "contacts_table")
data class Contact(
    @PrimaryKey(autoGenerate = false) val phoneNumber: String,
    val name: String,
) : Parcelable

@Entity(primaryKeys = ["groupId", "contactId"], tableName = "relationships_table")
data class Relationship(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    val groupId: Int,
    val contactId: String, // phone number
)

data class GroupWithContacts(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "contactID",
        associateBy = Junction(Relationship::class)
    )
    val contacts: List<Contact>,
)