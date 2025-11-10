package com.quickstart.s0001

import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant

fun sendMessageToClient(
    client: Client?, message: String?, mailer: Mailer
) {
    val email = client?.personalInfo?.email
    if(message !=null &&   email != null) {
        mailer.sendMessage(email, message);
    } else {
        println("Skip to send mail!!")
    }
}

class Client(val personalInfo: PersonalInfo?)
class PersonalInfo(val email: String?)
interface Mailer {
    fun sendMessage(email: String, message: String)
}

class MailSender :Mailer {
    override fun sendMessage(email:String, message: String) {
        val msg = """
          Sending mail to ${email} with message: ${message}
        """.trimIndent()

        println(msg)
        println("Mail has sent!")
    }
}


/**
 *  练习使用空判断
 */
fun main() {

    println("===> Null")
    sendMessageToClient(null, null, MailSender())


    println("===> NoneNull")
    sendMessageToClient(Client(PersonalInfo("test@163.com")), "Hell, test!", MailSender())


    ///
    val i = Random(2).nextInt() % 2
    var str: String? = null
    if(i == 0) { str = "test" }
    fun processNonNullString(str: String) :Unit {
        str.also {
            println("Process '$it' with length: ${it.length}")
        }
    }

    val length = str?.let {
        println("let() called on $it")
        processNonNullString(it)      // OK: 'it' is not null inside '?.let { }'
        it.length
    }
    println("length: $length")
}