package xyz.deseteral.djlama

import spock.lang.*

class DjLamaApplicationSpecification extends Specification {

    def "should pass"() {
        given:
        def a = 1
        def b = 1

        when:
        def sum = a + b

        then:
        sum == 2
    }
}
