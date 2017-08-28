import * as moment from "moment";
import {Component, OnInit} from "@angular/core";
import {extend, includes, map, pick, transform} from "lodash-es";

@Component({
    selector: 'analog-clock',
    template: `

        <div class="clocks active bounce">
            <article style="width: 250px; height: 250px;" class="clock ios7 js-tokyo">
                <section class="hours-container">
                    <section class="hours"></section>
                </section>
                <section class="minutes-container">
                    <section class="minutes"></section>
                </section>
                <section class="seconds-container">
                    <section class="seconds"></section>
                </section>
            </article>
        </div>

    `,
    styleUrls: ['./AnalogClock.component.scss']
})
export class AnalogClockComponent implements OnInit {

    ngOnInit(): void {
        this.initInternationalClocks();
        this.initLocalClocks();
        this.moveSecondHands();
        this.setUpMinuteHands();
    }


    initInternationalClocks() {

        const time = {
            jsclass: 'js-tokyo',
            jstime: moment()
        };

        // Initialise the clock settings and the three times
        const hours = Number.parseInt(time.jstime.format('h'));
        const minutes = Number.parseInt(time.jstime.format('mm'));
        const seconds = Number.parseInt(time.jstime.format('ss'));

        let degrees = [
            {
                hand: 'hours',
                degree: (hours * 30) + (minutes / 2)
            },
            {
                hand: 'minutes',
                degree: (minutes * 6)
            },
            {
                hand: 'seconds',
                degree: (seconds * 6)
            }
        ];
        for (let j = 0; j < degrees.length; j++) {
            const elements = document.querySelectorAll('.active .' + time.jsclass + ' .' + degrees[j].hand);
            for (let k = 0; k < elements.length; k++) {
                elements[k]['style'].webkitTransform = 'rotateZ(' + degrees[j].degree + 'deg)';
                elements[k]['style'].transform = 'rotateZ(' + degrees[j].degree + 'deg)';
                // If this is a minute hand, note the seconds position (to calculate minute position later)
                if (degrees[j].hand === 'minutes') {
                    elements[k].parentElement.setAttribute('data-second-angle', '' + degrees[j + 1].degree);
                }
            }
        }


        // Add a class to the clock container to show it
        let elements = document.querySelectorAll('.clock');
        for (let l = 0; l < elements.length; l++) {
            elements[l].className = elements[l].className + ' show';
        }


    }

    /*
     * Starts any clocks using the user's local time
     */
    initLocalClocks() {
        // Get the local time using JS
        let date = new Date;
        let seconds = date.getSeconds();
        let minutes = date.getMinutes();
        let hours = date.getHours();

        // Create an object with each hand and it's angle in degrees
        let hands = [
            {
                hand: 'hours',
                angle: (hours * 30) + (minutes / 2)
            },
            {
                hand: 'minutes',
                angle: (minutes * 6)
            },
            {
                hand: 'seconds',
                angle: (seconds * 6)
            }
        ];
        // Loop through each of these hands to set their angle
        for (let j = 0; j < hands.length; j++) {
            let elements = document.querySelectorAll('.local .' + hands[j].hand);
            for (let k = 0; k < elements.length; k++) {
                elements[k]['style'].transform = 'rotateZ(' + hands[j].angle + 'deg)';
                // If this is a minute hand, note the seconds position (to calculate minute position later)
                if (hands[j].hand === 'minutes') {
                    elements[k].parentElement.setAttribute('data-second-angle', '' + hands[j + 1].angle);
                }
            }
        }
    }

    /*
     * Move the second containers
     */
    moveSecondHands() {
        let containers = document.querySelectorAll('.bounce .seconds-container');
        setInterval(function () {
            for (let i = 0; i < containers.length; i++) {
                if (containers[i]['angle'] === undefined) {
                    containers[i]['angle'] = 6;
                } else {
                    containers[i]['angle'] += 6;
                }
                containers[i]['style'].webkitTransform = 'rotateZ(' + containers[i]['angle'] + 'deg)';
                containers[i]['style'].transform = 'rotateZ(' + containers[i]['angle'] + 'deg)';
            }
        }, 1000);
        for (let i = 0; i < containers.length; i++) {
            // Add in a little delay to make them feel more natural
            let randomOffset = Math.floor(Math.random() * (100 - 10 + 1)) + 10;
            containers[i]['style'].transitionDelay = '0.0' + randomOffset + 's';
        }
    }

    /*
     * Set a timeout for the first minute hand movement (less than 1 minute), then rotate it every minute after that
     */
    setUpMinuteHands() {
        // More tricky, this needs to move the minute hand when the second hand hits zero
        let containers = document.querySelectorAll('.minutes-container');
        let secondAngle = Number.parseInt(containers[containers.length - 1].getAttribute('data-second-angle'));
        if (secondAngle > 0) {
            // Set a timeout until the end of the current minute, to move the hand
            let delay = (((360 - secondAngle) / 6) + 0.1) * 1000;
            console.log(delay);
            setTimeout(function () {
                this.moveMinuteHands(containers);
            }, delay);
        }
    }

    moveMinuteHands(containers) {
        for (let i = 0; i < containers.length; i++) {
            containers[i].style.webkitTransform = 'rotateZ(6deg)';
            containers[i].style.transform = 'rotateZ(6deg)';
        }
        // Then continue with a 60 second interval
        setInterval(function () {
            for (let i = 0; i < containers.length; i++) {
                if (containers[i].angle === undefined) {
                    containers[i].angle = 12;
                } else {
                    containers[i].angle += 6;
                }
                containers[i].style.webkitTransform = 'rotateZ(' + containers[i].angle + 'deg)';
                containers[i].style.transform = 'rotateZ(' + containers[i].angle + 'deg)';
            }
        }, 60000);
    }

}


