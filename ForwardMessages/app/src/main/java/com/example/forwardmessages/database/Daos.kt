package com.example.forwardmessages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Daos {
//  Insert
    @Insert
    fun insertGroup(group: Group)
    @Insert
    fun insertContact(contact: Contact)
    @Insert
    fun insertRelationship(relationship: Relationship)

//  Delete
    @Delete
    fun deleteGroup(group: Group)
    @Delete
    fun deleteContact(contact: Contact)
    @Delete
    fun deleteRelationship(relationship: Relationship)

//  Update
    @Update
    fun update(group: Group)
    @Update
    fun update(contact: Contact)
    @Update
    fun update(relationship: Relationship)

//    Get group id
    @Query("SELECT id FROM groups_table WHERE name = :name AND phoneNumber = :phoneNumber AND contentText = :contentText")
    fun getGroupIdWithAllParameters(name: String, phoneNumber: String, contentText: String): Int

    @Query("SELECT id FROM groups_table WHERE name = :name AND phoneNumber = :phoneNumber")
    fun getGroupIdWithPhoneNumber(name: String, phoneNumber: String): Int

    @Query("SELECT id FROM groups_table WHERE name = :name AND contentText = :contentText")
    fun getGroupIdWithContentText(name: String, contentText: String): Int

    @Query("SELECT id FROM groups_table WHERE name = :name")
    fun getGroupIdWithName(name: String): Int

// Get Group By Id
    @Query("SELECT * FROM groups_table WHERE id = :groupId")
    fun getGroupById(groupId: Int): Flow<Group>

//  Get all Groups
    @Query("SELECT * FROM groups_table ORDER BY name ASC")
    fun getAllGroups(): Flow<List<Group>>

//  Get Group Contacts
    @Transaction
    @Query("SELECT * FROM contacts_table WHERE phoneNumber IN (SELECT contactId FROM relationships_table WHERE groupId = :groupId)")
    fun getContactsByGroupId(groupId: Int): Flow<List<Contact>>

//    Get Group By Condition
    @Transaction
    @Query("SELECT * FROM groups_table WHERE :contentText LIKE '%' || contentText || '%'")
    fun getGroupsByContext(contentText: String): Flow<List<Group>>
    @Transaction
    @Query("SELECT * FROM groups_table WHERE :phoneNumber = phoneNumber")
    fun getGroupsByPhoneNumber(phoneNumber: String): Flow<List<Group>>
    @Transaction
    @Query("SELECT * FROM groups_table WHERE (:contentText LIKE '%' || contentText || '%') AND (:phoneNumber = phoneNumber)")
    fun getGroupsByCondition(contentText: String, phoneNumber: String): Flow<List<Group>>

//   Check if Contact Exists
    @Query("SELECT EXISTS (SELECT * FROM contacts_table WHERE name = :name AND phoneNumber = :phoneNumber)")
    fun checkIsContactExists(name: String, phoneNumber: String): Flow<Boolean>

//    Check if Relationship Exists
    @Query("SELECT EXISTS (SELECT * FROM relationships_table WHERE groupId = :groupId AND contactId = :contactId)")
    fun checkIfRelationshipExists(groupId: Int, contactId: String): Flow<Boolean>

//    Get Relationship by group id
    @Query("SELECT * FROM relationships_table WHERE groupId = :groupId")
    fun getRelationshipsByGroupId(groupId: Int): Flow<List<Relationship>>

}




