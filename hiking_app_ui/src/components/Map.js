import {useState, useMemo, useCallback, useRef} from 'react';
import {
    GoogleMap,
    Marker,
    Polyline
} from '@react-google-maps/api';


export default function Map() {
    const [markers, setMarkers] = useState([]);
    const mapRef = useRef();
    const center = useMemo(() => ({lat: 43, lng: -80}), []);
    const options = useMemo(() => ( {
        disableDefaultUI: true,
        clickableIcons: false
    }), []);


    let mapString = "";

    for(let x = 0; x < markers.length; x++) {
        mapString = mapString + `${markers[x].lat},${markers[x].lng};`
    }


    
    const stringArray = mapString.trim().split(";");
    stringArray.pop();

    let newMarkers = [];

    for(let i = 0; i < stringArray.length; i++) {
        const coords = stringArray[i].split(",");
        const newMark = {
            lat: parseFloat(coords[0]),
            lng: parseFloat(coords[1])
        }
        newMarkers.push(newMark);
    }



    const onLoad = useCallback(map => (mapRef.current = map), []);

    const onMapClicked = (clickEvent) => {
        const updatedMarkers = [...markers];
        updatedMarkers.push({lat: clickEvent.latLng.lat(), lng: clickEvent.latLng.lng()});
        setMarkers(updatedMarkers);
    }

    return <div className="container">
        <div className="map">
            <GoogleMap 
                zoom={10} 
                center={center} 
                mapContainerClassName="map-container"
                options={options}
                onLoad={onLoad}
                onClick={onMapClicked}
            >
                {markers?.map((coords, index) => {
                    return <Marker key={index} position={coords} />
                })}
                {markers.length > 1 && 
                    <Polyline
                    path={markers}
                    strokeColor='0000ff'
                    strokeOpacity={0.8}
                    strokeWeight={6} />
                }
                
                
            </GoogleMap>
        </div>
    </div>
}