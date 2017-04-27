/**
 *  Vent Manager 7
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
    name: "Vent Manager 7",
    namespace: "trippd6",
    author: "James Donnelly",
    description: "Manage a vent based on room tempature",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)

preferences {
	section("Temperature") {
		// TODO: put inputs here
        input (name:"tempgoal", title: "Goal Tempature", type:"number", required: true)
        input "tempvent", "capability.temperatureMeasurement", required: true, title: "Vent Sensor"
        input "temproom", "capability.temperatureMeasurement", required: true, title: "Room Sensor"
	}
    
    section("Vents") {
		// TODO: put inputs here
        input "vent", "capability.switchLevel", required: true, title: "Vent"
        input (name:"ventopenmin", title: "Min Open Percentage", type:"number", required: true)
        input (name:"ventopenmax", title: "Max Open Percentage", type:"number", required: true)
        
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
    subscribe(tempvent, "temperature", tempChangeHandler)
    subscribe(temproom, "temperature", tempChangeHandler)
    
    adjustvent()
}

def tempChangeHandler(evt) {
    log.debug "Tempature Change called"
    
    adjustvent()
}

def adjustvent() {
	log.debug "Adjust Vent"

    int currentTempVent = tempvent.currentTemperature?.toFloat()?.round(1)
    int currentTempRoom = temproom.currentTemperature?.toFloat()?.round(1)
    
    if (!currentTempVent || !currentTempRoom) {
    	log.debug "Null tempature. Doing nothing."
        return
    }

    log.debug "Room Temp: $currentTempRoom"
    log.debug "Vent Temp: $currentTempVent"
    log.debug "Goal Temp: $tempgoal"
    
    int differencegoal = currentTempRoom - tempgoal
    
    int differencevent = currentTempRoom - currentTempVent
    
    if (differencegoal == 0) {
    	log.debug "Goal temp achived. Setting vent to minimum: $ventopenmin"
        sendNotificationEvent("Closing Vent. Goal temp achived. Setting vent to minimum: $ventopenmin")
 		vent.setLevel(ventopenmin)    
    } else if (differencegoal > 0 && differencevent > 0) {
        log.debug "Difference from goal $differencegoal, Difference from vent $differencevent, Setting vent to max: $ventopenmax"
        sendNotificationEvent("Opening Vent. Setting vent to max: $ventopenmax")
 		vent.setLevel(ventopenmax)    
    } else if (differencegoal < 0 && differencevent < 0) {
        log.debug "Difference from goal $differencegoal, Difference from vent $differencevent, Setting vent to max: $ventopenmax"
        sendNotificationEvent("Opening Vent. Setting vent to max: $ventopenmax")
 		vent.setLevel(ventopenmax)    
    } else {
    	log.debug "Vent temperature not same direction as goal. Setting vent to minimum: $ventopenmin"
        sendNotificationEvent("Closing Vent. Vent tempature is not the correct direction to achive goal. Setting vent to minimum: $ventopenmin")
 		vent.setLevel(ventopenmin)    
    }
}

// TODO: implement event handlers