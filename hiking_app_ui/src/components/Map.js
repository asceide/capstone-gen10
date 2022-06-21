import {useState, useMemo, useCallback, useRef, useEffect} from 'react';
import {
    GoogleMap,
    Marker,
    Polyline
} from '@react-google-maps/api';


export default function Map({mapString, onMapClicked, spotMarker}) {
    const [markers, setMarkers] = useState([]);
    const mapRef = useRef();
    let center = useMemo(() => ({lat: 39.8283, lng: -98.5795}), []);
    const options = useMemo(() => ( {
        disableDefaultUI: true,
        clickableIcons: false
    }), []);
 
    const onLoad = useCallback(map => (mapRef.current = map), []);

    // const onMapClicked = (clickEvent) => {
    //     const updatedMarkers = [...markers];
    //     updatedMarkers.push({lat: clickEvent.latLng.lat(), lng: clickEvent.latLng.lng()});
    //     setMarkers(updatedMarkers);
    // }

    useEffect(() => {
        if(mapString) {
            const stringArray = mapString.split(";");
            stringArray?.pop();
            let newMarkers = [];
    
            for(let i = 0; i < stringArray?.length; i++) {
                const coords = stringArray[i].split(",");
                const newMark = {
                    lat: parseFloat(coords[0]),
                    lng: parseFloat(coords[1])
                }
                newMarkers.push(newMark);
            }
            setMarkers(newMarkers);
            
        }
    }, [mapString]);

    

    return <div className="container">
        <div className="map">
            {markers ? <GoogleMap 
                zoom={10} 
                center={markers[0]} 
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

                {spotMarker && <Marker position={spotMarker} icon="https://hiking-app-photos.s3.amazonaws.com/darkgreen_MarkerS.png" />}
                
                
            </GoogleMap> 
            :
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

                {spotMarker && <Marker position={spotMarker} icon="https://hiking-app-photos.s3.amazonaws.com/darkgreen_MarkerS.png" />}
                
                
            </GoogleMap>}
            
        </div>
    </div>
}