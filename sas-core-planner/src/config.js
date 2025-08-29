const logout = () => {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    window.location.href = 'login.html';
}

const apiUrl = 'https://sas-core-planner-latest.onrender.com';

const cores = ['CCD', 'CCO', 'NS', 'SCL', 'HST', 'AHo', 'AHp', 'AHq', 'AHr', 'WCr', 'WCd', 'WC', 'QQ', 'QR'];

export { logout, apiUrl, cores };