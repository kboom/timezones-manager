package com.toptal.ggurgul.timezones.domain.models

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.ReadOnlyProperty
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ApiModel(value = "Timezone", description = "A representation of timezone")
@Entity
@Table(name = "TIMEZONES")
data class Timezone(

        @Id
        @Column(name = "ID")
        @SequenceGenerator(name = "timezone_seq", sequenceName = "timezone_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timezone_seq")
        @get:ApiModelProperty(readOnly = true, hidden = true)
        @get:ReadOnlyProperty
        var id: Long? = null,

        @Column(name = "NAME", length = 50)
        @get:NotNull
        @get:Size(min = 4, max = 50)
        var name: String? = null,

        @Column(name = "LOCATION_NAME", length = 50)
        @get:NotNull
        @get:Size(min = 4, max = 50)
        var locationName: String? = null,

        @Column(name = "DIFF_TO_GMT")
        @get:NotNull
        @get:Size(min = 0, max = 23)
        var differenceToGMT: Int? = null,

        @ManyToOne
        @get:NotNull
        @get:ReadOnlyProperty
        @get:ApiModelProperty(readOnly = true, hidden = true)
        var owner: User? = null

)