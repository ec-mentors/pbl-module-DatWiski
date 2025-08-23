import Icon from '../components/Icon';

const Login = () => {
  // Check for OAuth error in URL
  const urlParams = new URLSearchParams(window.location.search);
  const hasOAuthError = urlParams.get('error') === 'oauth_failed';

  const handleGoogleLogin = () => {
    window.location.href = '/oauth2/authorization/google';
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center p-8 relative overflow-hidden">
      <div className="max-w-sm w-full z-10 mx-auto">
        {/* Logo and Header */}
        <div className="text-center mb-8">
          <div className="w-16 h-16 mx-auto mb-6 bg-gradient-to-br from-green-500 to-purple-600 rounded-xl flex items-center justify-center shadow-xl transition-all duration-300">
            <Icon name="money" size={32} color="white" />
          </div>
          
          <h1 className="text-3xl font-extrabold text-white mb-4 tracking-tight leading-tight">
            BudgetTracker
          </h1>
        </div>

        {/* Login Card */}
        <div className="bg-white/10 backdrop-blur-lg border border-white/20 rounded-xl w-full max-w-sm mx-auto p-6">
            <div className="text-center mb-6">
              <h3 className="text-2xl font-bold text-white mb-4">
                Welcome Back
              </h3>
              <p className="text-gray-300 text-lg font-medium">
                Sign in
              </p>
                
              {hasOAuthError && (
                <div className="mt-6 p-6 bg-red-500/10 border border-red-500/40 rounded-lg">
                  <div className="flex items-center justify-center mb-2 gap-2">
                    <Icon name="alert" size={20} color="#ef4444" />
                    <span className="text-red-500 font-bold text-lg">Development Mode</span>
                  </div>
                  <p className="text-red-500 text-sm leading-relaxed">
                    OAuth2 not configured with real Google credentials. Using dummy credentials for development testing.
                  </p>
                </div>
              )}
            </div>
            
            <button
              onClick={handleGoogleLogin}
              className="w-64 mx-auto flex items-center justify-center py-3 px-6 bg-white text-gray-800 font-semibold text-base rounded-lg border-0 cursor-pointer shadow-md transition-all duration-300 gap-3 hover:shadow-lg hover:scale-105"
            >
              <svg className="w-5 h-5" viewBox="0 0 24 24">
                <path
                  fill="#4285F4"
                  d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                />
                <path
                  fill="#34A853"
                  d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                />
                <path
                  fill="#FBBC05"
                  d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                />
                <path
                  fill="#EA4335"
                  d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                />
              </svg>
              <span>Continue with Google</span>
            </button>
        </div>
      </div>
    </div>
  );
};

export default Login;