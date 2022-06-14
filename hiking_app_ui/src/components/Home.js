import { useContext } from 'react';
import AuthContext from '../context/AuthContext';

export default function Home(){

const { user } = useContext(AuthContext);

console.log(user);

    return(
        <div>
            <h1>Hiking Home / ハイキングホーム</h1>
        </div>
 )
}