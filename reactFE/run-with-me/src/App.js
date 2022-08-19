import { BrowserRouter } from "react-router-dom";
import "./App.css";
import Main from "./page/Main";
function App() {
  return (
    <>
      <BrowserRouter>
        <Main></Main>
      </BrowserRouter>
    </>
  );
}

export default App;
