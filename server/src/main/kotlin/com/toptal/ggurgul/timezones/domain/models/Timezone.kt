package com.toptal.ggurgul.timezones.domain.models

import com.toptal.ggurgul.timezones.domain.models.security.User
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "TIMEZONES")
data class Timezone(

        @Id
        @Column(name = "ID")
        @SequenceGenerator(name = "timezone_seq", sequenceName = "timezone_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timezone_seq")
        private var id: Long? = null,

        @Column(name = "NAME", length = 50)
        @NotNull
        @Size(min = 4, max = 50)
        private var name: String? = null,

        @Column(name = "LOCATION_NAME", length = 50)
        @NotNull
        @Size(min = 4, max = 50)
        private var locationName: String? = null,

        @Column(name = "DIFF_TO_GMT")
        @NotNull
        @Size(min = 0, max = 23)
        private var differenceToGMT: Int? = null,

        @ManyToOne
        @NotNull
        private var owner: User? = null

)