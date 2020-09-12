package com.natchuz.hub.backend.users

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import java.util.*

private fun defaultUser(uuid: UUID): Document =
        Document().apply {
            append("uuid", uuid.toString())
            append("rank", "DEFAULT")
            append("friends", emptyList<Document>())
            append("invitedFriends", emptyList<Document>())
            append("pendingFriends", emptyList<Document>())
        }

class DefaultService private constructor(
        private val usersCollection: MongoCollection<Document>
) : Service {

    override fun getUser(playerUUID: UUID): String {
        val user = usersCollection
                .find(Document("uuid", playerUUID))
                .firstOrNull()
        return if (user == null) {
            val newUser = defaultUser(playerUUID)
            usersCollection.insertOne(newUser)
            newUser.toJson()
        } else {
            user.toJson()
        }
    }

    companion object Factory {
        fun construct(
                connectionUrl: String,
                databaseName: String,
                usersCollectionName: String,
        ): Service {
            val client = MongoClients.create(connectionUrl)
            val database = client.getDatabase(databaseName)

            val usersCollection = database.getCollection(usersCollectionName)
            return DefaultService(usersCollection)
        }
    }
}