import { useNavigate } from 'react-router-dom';

const logout = () => {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
}

export default logout;
