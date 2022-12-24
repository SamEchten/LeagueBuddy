package com.leaguebuddy.sql

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.BoringLayout
import android.util.Log
import com.leaguebuddy.dataClasses.UserV2
import com.leaguebuddy.exceptions.NoCredentialsException

class SqlDbHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE $TABLE_USER ($ID INTEGER PRIMARY KEY, $SN_ID TEXT, $S_ID TEXT, $D_ID TEXT, $PROFILE_PIC INTEGER)")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    fun addUser(summonerName: String, summonerId : String, discordId : String,  profilePicId: Int){
        val values = ContentValues()
        values.put(SN_ID, summonerName)
        values.put(S_ID, summonerId)
        values.put(D_ID, discordId)
        values.put(PROFILE_PIC, profilePicId)

        val db = this.writableDatabase

        if(!userHasCredentials()){
            db.insert(TABLE_USER, null, values)
            db.close()
        }else {
            updateCredentials(summonerName, summonerId, discordId, profilePicId)
        }
    }

    /**
     * Update the user credentials sync this with firebase database once use is logged in.
     * @param columnValue summonerName, summonerId, discordId, profilePicId
     */
    fun updateSingleCredential(columnValue : String, column : String) {
        val values =  ContentValues()
        values.put(column, columnValue)
        val db = this.writableDatabase

        db.update(TABLE_USER, values, "summonerName = ?", arrayOf(columnValue))
    }

    /**
     * Update all user credentials sync this with firebase database once use is logged in.
     * @param summonerName
     * @param summonerId
     * @param discordId
     * @param profilePicId
     */
    fun updateCredentials(summonerName: String, summonerId : String, discordId : String,  profilePicId: Int) {
        val values = ContentValues()
        values.put(SN_ID, summonerName)
        values.put(S_ID, summonerId)
        values.put(D_ID, discordId)
        values.put(PROFILE_PIC, profilePicId)
        val db = this.writableDatabase

        db.update(TABLE_USER, values, "summonerName = ?", arrayOf(summonerName))

        db.close()
    }

    private fun userHasCredentials() : Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_USER", null)
        return result.count > 0
    }

    fun getCurrentUserCredentials() : UserV2{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_USER", null)
        if(result.count > 0) {
            val summonerName = result.getString(result.getColumnIndex("summonerName"))
            val summonerId = result.getString(result.getColumnIndex("summonerId"))
            val discordId = result.getString(result.getColumnIndex("discord"))
            val profilePicId = result.getString(result.getColumnIndex("profilePicId"))

            db.close()
            return UserV2(
                summonerName,
                summonerId,
                discordId,
                profilePicId.toInt()
            )
        }else {
            Log.e("TAG", "User has no credentials in the current database, Trying to redirect to login")
            throw NoCredentialsException("User has no credentials saved")
        }
    }

    companion object {
        private const val DATABASE_NAME = "LEAGUE_BUDDY"
        private const val DATABASE_VERSION  = 1
        const val TABLE_USER = "user"
        const val ID = "id"
        const val SN_ID = "summonerName"
        const val S_ID = "summonerId"
        const val D_ID = "discordId"
        const val PROFILE_PIC = "profilePicId"

    }
}