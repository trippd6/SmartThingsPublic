/**
 *  Vent Manager
 *
 *  Copyright 2016 James Donnelly
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Vent Manager 6",
    namespace: "trippd6",
    author: "James Donnelly",
    description: "Opens and closes vent based on room tempature",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Vent temp sensor") {
		// TODO: put inputs here
        input "venttemp", "capability.temperatureMeasurement", required: true, title: "Sensor?"
        
	}
    section("Room Temp Sensor") {
		// TODO: put inputs here
        input "roomtemp", "capability.temperatureMeasurement", required: true, title: "Sensor?"
	}
    section("Vent") {
		// TODO: put inputs here
        input "vent", "capability.switchLevel", required: true, title: "Vent?"
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    subscribe(venttemp, "temperature", tempChangeHandler)
    subscribe(roomtemp, "temperature", tempChangeHandler)
}

def tempChangeHandler(evt) {
    log.debug "Tempature Change called: $evt"
}
// TODO: implement event handlers