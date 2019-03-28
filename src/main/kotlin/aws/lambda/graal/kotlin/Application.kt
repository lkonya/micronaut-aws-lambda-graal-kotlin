package aws.lambda.graal.kotlin

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("aws.lambda.graal.kotlin")
                .mainClass(Application.javaClass)
                .start()
    }
}